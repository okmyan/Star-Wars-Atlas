package com.okmyan.starwarsatlas.feature.starships.data

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.okmyan.starwarsatlas.core.paging.CursorPage
import com.okmyan.starwarsatlas.core.paging.CursorPagingSource
import com.okmyan.starwarsatlas.feature.starships.domain.StarshipListItem
import com.okmyan.starwarsatlas.graphql.StarshipsQuery
import com.okmyan.starwarsatlas.utils.requireData

class StarshipsPagingSource(
    apolloClient: ApolloClient,
) : CursorPagingSource<StarshipListItem>(apolloClient) {

    override suspend fun fetch(loadSize: Int, cursor: String?): CursorPage<StarshipListItem> {
        val data = apolloClient.query(
            StarshipsQuery(
                first = Optional.Present(loadSize),
                after = Optional.presentIfNotNull(cursor),
            )
        ).execute().requireData()

        val starships = data.allStarships?.starships
            .orEmpty()
            .filterNotNull()
            .mapNotNull { starship ->
                val name = starship.name ?: return@mapNotNull null
                StarshipListItem(
                    id = starship.id,
                    name = name,
                    filmCount = starship.filmConnection?.films?.filterNotNull()?.size ?: 0,
                )
            }

        val nextCursor = data.allStarships?.pageInfo
            ?.takeIf { it.hasNextPage }
            ?.endCursor

        return CursorPage(items = starships, nextCursor = nextCursor)
    }

}
