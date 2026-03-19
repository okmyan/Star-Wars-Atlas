package com.okmyan.starwarsatlas.feature.starships.data

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.okmyan.starwarsatlas.feature.starships.domain.StarshipListItem
import com.okmyan.starwarsatlas.graphql.StarshipsQuery
import javax.inject.Inject

class StarshipsRepository @Inject constructor(
    private val apolloClient: ApolloClient,
) {

    suspend fun getStarships(): List<StarshipListItem> {
        val response = apolloClient.query(
            StarshipsQuery(
                first = Optional.Present(20),
                after = Optional.Absent,
            )
        ).execute()

        response.exception?.let { throw it }
        val data =
            response.data ?: error("StarshipsQuery returned null data. Errors: ${response.errors}")

        return data.allStarships?.starships
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
    }

}
