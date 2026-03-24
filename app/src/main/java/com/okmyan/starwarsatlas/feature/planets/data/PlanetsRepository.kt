package com.okmyan.starwarsatlas.feature.planets.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.apollographql.apollo.ApolloClient
import com.okmyan.starwarsatlas.core.datastore.LastRefreshDataStore
import com.okmyan.starwarsatlas.core.model.Outcome
import com.okmyan.starwarsatlas.feature.planets.data.database.PlanetsDao
import com.okmyan.starwarsatlas.feature.planets.domain.PlanetDetails
import com.okmyan.starwarsatlas.feature.planets.domain.PlanetListItem
import com.okmyan.starwarsatlas.graphql.PlanetDetailsQuery
import com.okmyan.starwarsatlas.utils.PAGE_SIZE
import com.okmyan.starwarsatlas.utils.outcomeOf
import com.okmyan.starwarsatlas.utils.requireData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PlanetsRepository @Inject constructor(
    private val apolloClient: ApolloClient,
    private val planetsDao: PlanetsDao,
    private val lastRefreshDataStore: LastRefreshDataStore,
) {

    @OptIn(ExperimentalPagingApi::class)
    fun getPlanets(): Flow<PagingData<PlanetListItem>> = Pager(
        config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
        remoteMediator = PlanetsRemoteMediator(apolloClient, planetsDao, lastRefreshDataStore),
        pagingSourceFactory = { planetsDao.pagingSource() },
    ).flow.map { pagingData ->
        pagingData.map { entity ->
            PlanetListItem(
                id = entity.id,
                name = entity.name,
                filmCount = entity.filmCount,
            )
        }
    }

    suspend fun getPlanetDetails(id: String): Outcome<PlanetDetails> = outcomeOf {
        val data = apolloClient.query(
            PlanetDetailsQuery(id = id)
        ).execute().requireData()

        data.planet?.run {
            PlanetDetails(
                name = name,
                diameter = diameter,
                rotationPeriod = rotationPeriod,
                orbitalPeriod = orbitalPeriod,
                gravity = gravity,
                population = population,
                climates = climates.orEmpty().filterNotNull(),
                terrains = terrains.orEmpty().filterNotNull(),
                surfaceWater = surfaceWater,
                films = filmConnection?.films.orEmpty().filterNotNull().mapNotNull { it.title },
                residents = residentConnection?.residents.orEmpty().filterNotNull()
                    .mapNotNull { it.name },
            )
        } ?: error("Planet not found")
    }

}
