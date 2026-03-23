package com.okmyan.starwarsatlas.feature.people.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.okmyan.starwarsatlas.graphql.PeopleQuery
import com.okmyan.starwarsatlas.utils.mediatorResultOf
import com.okmyan.starwarsatlas.utils.requireData
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
class PeopleRemoteMediator(
    private val apolloClient: ApolloClient,
    private val peopleDao: PeopleDao,
    private val lastRefreshDao: PeopleLastRefreshDao,
) : RemoteMediator<Int, PersonEntity>() {

    override suspend fun initialize(): InitializeAction {
        val lastRefresh = lastRefreshDao.lastRefresh()?.timestamp
            ?: return InitializeAction.LAUNCH_INITIAL_REFRESH

        return if (System.currentTimeMillis() - lastRefresh < CACHE_TIMEOUT) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PersonEntity>,
    ): MediatorResult = mediatorResultOf {
        val cursor: String? = when (loadType) {
            LoadType.REFRESH -> null

            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)

            LoadType.APPEND -> {
                val lastItem = state.lastItemOrNull()
                    ?: return MediatorResult.Success(endOfPaginationReached = peopleDao.count() == 0)

                peopleDao.remoteKeyById(lastItem.id)?.nextCursor
                    ?: return MediatorResult.Success(endOfPaginationReached = true)
            }
        }

        val data = apolloClient.query(
            PeopleQuery(
                first = Optional.Present(state.config.pageSize),
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

        val remoteKeys = people.map {
            PeopleRemoteKeyEntity(id = it.id, nextCursor = nextCursor)
        }
        peopleDao.upsert(
            people = people,
            keys = remoteKeys,
            clearFirst = loadType == LoadType.REFRESH,
        )
        if (loadType == LoadType.REFRESH) {
            lastRefreshDao.upsertLastRefresh(PeopleLastRefreshEntity(timestamp = System.currentTimeMillis()))
        }

        MediatorResult.Success(endOfPaginationReached = nextCursor == null)
    }

    companion object {
        private val CACHE_TIMEOUT = TimeUnit.HOURS.toMillis(1)
    }

}
