package com.okmyan.starwarsatlas.feature.starships.data

import androidx.paging.ExperimentalPagingApi
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.okmyan.starwarsatlas.core.datastore.LastRefreshDataStore
import com.okmyan.starwarsatlas.core.paging.CursorPage
import com.okmyan.starwarsatlas.core.paging.CursorRemoteMediator
import com.okmyan.starwarsatlas.feature.starships.data.database.StarshipEntity
import com.okmyan.starwarsatlas.feature.starships.data.database.StarshipRemoteKeyEntity
import com.okmyan.starwarsatlas.feature.starships.data.database.StarshipsDao
import com.okmyan.starwarsatlas.graphql.StarshipsQuery
import com.okmyan.starwarsatlas.utils.requireData

@OptIn(ExperimentalPagingApi::class)
class StarshipsRemoteMediator(
    private val apolloClient: ApolloClient,
    private val starshipsDao: StarshipsDao,
    lastRefreshDataStore: LastRefreshDataStore,
) : CursorRemoteMediator<StarshipEntity>(lastRefreshDataStore, FEATURE_KEY) {

    override suspend fun getCount() = starshipsDao.count()

    override suspend fun getNextCursor(lastItem: StarshipEntity) =
        starshipsDao.remoteKeyById(lastItem.id)?.nextCursor

    override suspend fun fetch(pageSize: Int, cursor: String?): CursorPage<StarshipEntity> {
        val data = apolloClient.query(
            StarshipsQuery(
                first = Optional.Present(pageSize),
                after = Optional.presentIfNotNull(cursor),
            )
        ).execute().requireData()

        val allStarships = data.allStarships ?: error("allStarships is null")

        val starships = allStarships.starships
            .orEmpty()
            .filterNotNull()
            .mapNotNull { starship ->
                val name = starship.name ?: return@mapNotNull null
                StarshipEntity(
                    id = starship.id,
                    name = name,
                    filmCount = starship.filmConnection?.totalCount ?: 0,
                )
            }

        val nextCursor = allStarships.pageInfo
            .takeIf { it.hasNextPage }
            ?.endCursor

        return CursorPage(items = starships, nextCursor = nextCursor)
    }

    override suspend fun save(items: List<StarshipEntity>, nextCursor: String?, clearFirst: Boolean) {
        val remoteKeys = items.map { StarshipRemoteKeyEntity(id = it.id, nextCursor = nextCursor) }
        starshipsDao.save(starships = items, keys = remoteKeys, clearFirst = clearFirst)
    }

    companion object {
        private const val FEATURE_KEY = "starships"
    }

}
