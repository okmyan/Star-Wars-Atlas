package com.okmyan.starwarsatlas.feature.people.data

import androidx.paging.ExperimentalPagingApi
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.okmyan.starwarsatlas.core.datastore.LastRefreshDataStore
import com.okmyan.starwarsatlas.core.paging.CursorPage
import com.okmyan.starwarsatlas.core.paging.CursorRemoteMediator
import com.okmyan.starwarsatlas.feature.people.data.database.PeopleDao
import com.okmyan.starwarsatlas.feature.people.data.database.PeopleRemoteKeyEntity
import com.okmyan.starwarsatlas.feature.people.data.database.PersonEntity
import com.okmyan.starwarsatlas.graphql.PeopleQuery
import com.okmyan.starwarsatlas.utils.requireData

@OptIn(ExperimentalPagingApi::class)
class PeopleRemoteMediator(
    private val apolloClient: ApolloClient,
    private val peopleDao: PeopleDao,
    lastRefreshDataStore: LastRefreshDataStore,
) : CursorRemoteMediator<PersonEntity>(lastRefreshDataStore, FEATURE_KEY) {

    override suspend fun getCount() = peopleDao.count()

    override suspend fun getNextCursor(lastItem: PersonEntity) =
        peopleDao.remoteKeyById(lastItem.id)?.nextCursor

    override suspend fun fetch(pageSize: Int, cursor: String?): CursorPage<PersonEntity> {
        val data = apolloClient.query(
            PeopleQuery(
                first = Optional.Present(pageSize),
                after = Optional.presentIfNotNull(cursor),
            )
        ).execute().requireData()

        val allPeople = data.allPeople ?: error("allPeople is null")

        val people = allPeople.people
            .orEmpty()
            .filterNotNull()
            .mapNotNull { person ->
                val name = person.name ?: return@mapNotNull null
                PersonEntity(
                    id = person.id,
                    name = name,
                    filmCount = person.filmConnection?.totalCount ?: 0,
                )
            }

        val nextCursor = allPeople.pageInfo
            .takeIf { it.hasNextPage }
            ?.endCursor

        return CursorPage(items = people, nextCursor = nextCursor)
    }

    override suspend fun save(items: List<PersonEntity>, nextCursor: String?, clearFirst: Boolean) {
        val remoteKeys = items.map { PeopleRemoteKeyEntity(id = it.id, nextCursor = nextCursor) }
        peopleDao.save(
            people = items,
            keys = remoteKeys,
            clearFirst = clearFirst,
        )
    }

    companion object {
        private const val FEATURE_KEY = "people"
    }

}
