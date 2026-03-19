package com.okmyan.starwarsatlas.feature.planets.data

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.okmyan.starwarsatlas.feature.planets.domain.PlanetListItem
import com.okmyan.starwarsatlas.graphql.PlanetsQuery
import javax.inject.Inject

class PlanetsRepository @Inject constructor(
    private val apolloClient: ApolloClient,
) {

    suspend fun getPlanets(): List<PlanetListItem> {
        val response = apolloClient.query(
            PlanetsQuery(
                first = Optional.Present(20),
                after = Optional.Absent,
            )
        ).execute()

        response.exception?.let { throw it }
        val data =
            response.data ?: error("PlanetsQuery returned null data. Errors: ${response.errors}")

        return data.allPlanets?.planets
            .orEmpty()
            .filterNotNull()
            .mapNotNull { planet ->
                val name = planet.name ?: return@mapNotNull null

                PlanetListItem(
                    id = planet.id,
                    name = name,
                    filmCount = planet.filmConnection?.films?.filterNotNull()?.size ?: 0,
                )
            }
    }

}
