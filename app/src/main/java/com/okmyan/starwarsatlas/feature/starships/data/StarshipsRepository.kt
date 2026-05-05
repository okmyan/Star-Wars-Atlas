package com.okmyan.starwarsatlas.feature.starships.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.apollographql.apollo.ApolloClient
import com.okmyan.starwarsatlas.core.datastore.LastRefreshDataStore
import com.okmyan.starwarsatlas.core.model.Outcome
import com.okmyan.starwarsatlas.feature.starships.data.database.StarshipsDao
import com.okmyan.starwarsatlas.feature.starships.domain.StarshipDetails
import com.okmyan.starwarsatlas.feature.starships.domain.StarshipListItem
import com.okmyan.starwarsatlas.graphql.StarshipDetailsQuery
import com.okmyan.starwarsatlas.utils.PAGE_SIZE
import com.okmyan.starwarsatlas.utils.outcomeOf
import com.okmyan.starwarsatlas.utils.requireData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.collections.immutable.toImmutableList
import javax.inject.Inject

class StarshipsRepository @Inject constructor(
    private val apolloClient: ApolloClient,
    private val starshipsDao: StarshipsDao,
    private val lastRefreshDataStore: LastRefreshDataStore,
) {

    @OptIn(ExperimentalPagingApi::class)
    fun getStarships(): Flow<PagingData<StarshipListItem>> = Pager(
        config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
        remoteMediator = StarshipsRemoteMediator(apolloClient, starshipsDao, lastRefreshDataStore),
        pagingSourceFactory = { starshipsDao.pagingSource() },
    ).flow.map { pagingData ->
        pagingData.map { entity ->
            StarshipListItem(
                id = entity.id,
                name = entity.name,
                filmCount = entity.filmCount,
            )
        }
    }

    suspend fun getStarshipDetails(id: String): Outcome<StarshipDetails> = outcomeOf {
        val data = apolloClient.query(
            StarshipDetailsQuery(id = id)
        ).execute().requireData()

        data.starship?.run {
            StarshipDetails(
                name = name,
                model = model,
                starshipClass = starshipClass,
                manufacturers = manufacturers.orEmpty().filterNotNull().toImmutableList(),
                costInCredits = costInCredits,
                length = length,
                crew = crew,
                passengers = passengers,
                maxAtmosphericSpeed = maxAtmospheringSpeed,
                hyperdriveRating = hyperdriveRating,
                mglt = MGLT,
                cargoCapacity = cargoCapacity,
                consumables = consumables,
                pilots = pilotConnection?.pilots.orEmpty().filterNotNull().mapNotNull { it.name }
                    .toImmutableList(),
                films = filmConnection?.films.orEmpty().filterNotNull().mapNotNull { it.title }
                    .toImmutableList(),
            )
        } ?: error("Starship not found")
    }

}
