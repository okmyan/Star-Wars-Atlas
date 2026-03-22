package com.okmyan.starwarsatlas.feature.planets.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.apollographql.apollo.ApolloClient
import com.okmyan.starwarsatlas.core.model.Outcome
import com.okmyan.starwarsatlas.feature.planets.domain.PlanetDetails
import com.okmyan.starwarsatlas.feature.planets.domain.PlanetListItem
import com.okmyan.starwarsatlas.graphql.PlanetDetailsQuery
import com.okmyan.starwarsatlas.utils.PAGE_SIZE
import com.okmyan.starwarsatlas.utils.outcomeOf
import com.okmyan.starwarsatlas.utils.requireData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PlanetsRepository @Inject constructor(
    private val apolloClient: ApolloClient,
) {

    fun getPlanets(): Flow<PagingData<PlanetListItem>> = Pager(
        config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
        pagingSourceFactory = { PlanetsPagingSource(apolloClient) },
    ).flow

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
