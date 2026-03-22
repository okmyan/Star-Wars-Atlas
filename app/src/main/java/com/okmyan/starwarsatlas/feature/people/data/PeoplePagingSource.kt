package com.okmyan.starwarsatlas.feature.people.data

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.okmyan.starwarsatlas.core.paging.CursorPage
import com.okmyan.starwarsatlas.core.paging.CursorPagingSource
import com.okmyan.starwarsatlas.feature.people.domain.PersonListItem
import com.okmyan.starwarsatlas.graphql.PeopleQuery
import com.okmyan.starwarsatlas.utils.requireData

class PeoplePagingSource(
    apolloClient: ApolloClient,
) : CursorPagingSource<PersonListItem>(apolloClient) {

    override suspend fun fetch(loadSize: Int, cursor: String?): CursorPage<PersonListItem> {
        val data = apolloClient.query(
            PeopleQuery(
                first = Optional.Present(loadSize),
                after = Optional.presentIfNotNull(cursor),
            )
        ).execute().requireData()

        val people = data.allPeople?.people
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

        val nextCursor = data.allPeople?.pageInfo
            ?.takeIf { it.hasNextPage }
            ?.endCursor

        return CursorPage(items = people, nextCursor = nextCursor)
    }

}
