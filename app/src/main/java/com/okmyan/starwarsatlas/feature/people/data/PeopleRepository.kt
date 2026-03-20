package com.okmyan.starwarsatlas.feature.people.data

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.okmyan.starwarsatlas.core.model.Outcome
import com.okmyan.starwarsatlas.feature.people.domain.PersonListItem
import com.okmyan.starwarsatlas.graphql.PeopleQuery
import com.okmyan.starwarsatlas.utils.PAGE_SIZE
import com.okmyan.starwarsatlas.utils.outcomeOf
import com.okmyan.starwarsatlas.utils.requireData
import javax.inject.Inject

class PeopleRepository @Inject constructor(
    private val apolloClient: ApolloClient,
) {

    suspend fun getPeople(): Outcome<List<PersonListItem>> {
        return outcomeOf {
            val data = apolloClient.query(
                PeopleQuery(
                    first = Optional.Present(PAGE_SIZE),
                    after = Optional.Absent,
                )
            ).execute().requireData()

            data.allPeople?.people
                .orEmpty()
                .filterNotNull()
                .mapNotNull { person ->
                    val name = person.name ?: return@mapNotNull null
                    PersonListItem(
                        id = person.id,
                        name = name,
                        filmCount = person.filmConnection?.films?.filterNotNull()?.size ?: 0,
                    )
                }
        }
    }

}
