package com.okmyan.starwarsatlas.feature.starships.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.apollographql.apollo.ApolloClient
import com.okmyan.starwarsatlas.core.model.Outcome
import com.okmyan.starwarsatlas.feature.starships.domain.StarshipDetails
import com.okmyan.starwarsatlas.feature.starships.domain.StarshipListItem
import com.okmyan.starwarsatlas.graphql.StarshipDetailsQuery
import com.okmyan.starwarsatlas.utils.PAGE_SIZE
import com.okmyan.starwarsatlas.utils.outcomeOf
import com.okmyan.starwarsatlas.utils.requireData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StarshipsRepository @Inject constructor(
    private val apolloClient: ApolloClient,
) {

    fun getStarships(): Flow<PagingData<StarshipListItem>> = Pager(
        config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
        pagingSourceFactory = { StarshipsPagingSource(apolloClient) },
    ).flow

    suspend fun getStarshipDetails(id: String): Outcome<StarshipDetails> = outcomeOf {
        val data = apolloClient.query(
            StarshipDetailsQuery(id = id)
        ).execute().requireData()

        data.starship?.run {
            StarshipDetails(
                name = name,
                model = model,
                starshipClass = starshipClass,
                manufacturers = manufacturers.orEmpty().filterNotNull(),
                costInCredits = costInCredits,
                length = length,
                crew = crew,
                passengers = passengers,
                maxAtmosphericSpeed = maxAtmospheringSpeed,
                hyperdriveRating = hyperdriveRating,
                mglt = MGLT,
                cargoCapacity = cargoCapacity,
                consumables = consumables,
                pilots = pilotConnection?.pilots.orEmpty().filterNotNull().mapNotNull { it.name },
                films = filmConnection?.films.orEmpty().filterNotNull().mapNotNull { it.title },
            )
        } ?: error("Starship not found")
    }

}
