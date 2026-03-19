package com.okmyan.starwarsatlas.feature.people.data

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.okmyan.starwarsatlas.feature.people.domain.PersonListItem
import com.okmyan.starwarsatlas.graphql.PeopleQuery
import javax.inject.Inject

class PeopleRepository @Inject constructor(
    private val apolloClient: ApolloClient,
) {

    suspend fun getPeople(): List<PersonListItem> {
        val response = apolloClient.query(
            PeopleQuery(
                first = Optional.Present(20),
                after = Optional.Absent,
            )
        ).execute()

        response.exception?.let { throw it }
        val data =
            response.data ?: error("PeopleQuery returned null data. Errors: ${response.errors}")

        return data.allPeople?.people
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
