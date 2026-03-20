package com.okmyan.starwarsatlas.feature.starships.data

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.okmyan.starwarsatlas.core.model.Outcome
import com.okmyan.starwarsatlas.feature.starships.domain.StarshipListItem
import com.okmyan.starwarsatlas.graphql.StarshipsQuery
import com.okmyan.starwarsatlas.utils.PAGE_SIZE
import com.okmyan.starwarsatlas.utils.outcomeOf
import com.okmyan.starwarsatlas.utils.requireData
import javax.inject.Inject

class StarshipsRepository @Inject constructor(
    private val apolloClient: ApolloClient,
) {

    suspend fun getStarships(): Outcome<List<StarshipListItem>> {
        return outcomeOf {
            val data = apolloClient.query(
                StarshipsQuery(
                    first = Optional.Present(PAGE_SIZE),
                    after = Optional.Absent,
                )
            ).execute().requireData()

            data.allStarships?.starships
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

}
