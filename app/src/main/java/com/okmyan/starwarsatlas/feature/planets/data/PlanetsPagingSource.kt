package com.okmyan.starwarsatlas.feature.planets.data

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.okmyan.starwarsatlas.core.paging.CursorPage
import com.okmyan.starwarsatlas.core.paging.CursorPagingSource
import com.okmyan.starwarsatlas.feature.planets.domain.PlanetListItem
import com.okmyan.starwarsatlas.graphql.PlanetsQuery
import com.okmyan.starwarsatlas.utils.requireData

class PlanetsPagingSource(
    apolloClient: ApolloClient,
) : CursorPagingSource<PlanetListItem>(apolloClient) {

    override suspend fun fetch(loadSize: Int, cursor: String?): CursorPage<PlanetListItem> {
        val data = apolloClient.query(
            PlanetsQuery(
                first = Optional.Present(loadSize),
                after = Optional.presentIfNotNull(cursor),
            )
        ).execute().requireData()

        val planets = data.allPlanets?.planets
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

        val nextCursor = data.allPlanets?.pageInfo
            ?.takeIf { it.hasNextPage }
            ?.endCursor

        return CursorPage(items = planets, nextCursor = nextCursor)
    }

}
