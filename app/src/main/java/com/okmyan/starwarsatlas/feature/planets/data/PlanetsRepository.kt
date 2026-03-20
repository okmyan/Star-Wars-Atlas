package com.okmyan.starwarsatlas.feature.planets.data

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.okmyan.starwarsatlas.core.model.Outcome
import com.okmyan.starwarsatlas.feature.planets.domain.PlanetListItem
import com.okmyan.starwarsatlas.graphql.PlanetsQuery
import com.okmyan.starwarsatlas.utils.PAGE_SIZE
import com.okmyan.starwarsatlas.utils.outcomeOf
import com.okmyan.starwarsatlas.utils.requireData
import javax.inject.Inject

class PlanetsRepository @Inject constructor(
    private val apolloClient: ApolloClient,
) {

    suspend fun getPlanets(): Outcome<List<PlanetListItem>> {
        return outcomeOf {
            val data = apolloClient.query(
                PlanetsQuery(
                    first = Optional.Present(PAGE_SIZE),
                    after = Optional.Absent,
                )
            ).execute().requireData()

            data.allPlanets?.planets
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

}
