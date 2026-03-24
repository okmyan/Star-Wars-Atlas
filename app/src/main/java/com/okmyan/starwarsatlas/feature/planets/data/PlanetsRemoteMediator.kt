package com.okmyan.starwarsatlas.feature.planets.data

import androidx.paging.ExperimentalPagingApi
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.okmyan.starwarsatlas.core.datastore.LastRefreshDataStore
import com.okmyan.starwarsatlas.core.paging.CursorPage
import com.okmyan.starwarsatlas.core.paging.CursorRemoteMediator
import com.okmyan.starwarsatlas.feature.planets.data.database.PlanetEntity
import com.okmyan.starwarsatlas.feature.planets.data.database.PlanetRemoteKeyEntity
import com.okmyan.starwarsatlas.feature.planets.data.database.PlanetsDao
import com.okmyan.starwarsatlas.graphql.PlanetsQuery
import com.okmyan.starwarsatlas.utils.requireData

@OptIn(ExperimentalPagingApi::class)
class PlanetsRemoteMediator(
    private val apolloClient: ApolloClient,
    private val planetsDao: PlanetsDao,
    lastRefreshDataStore: LastRefreshDataStore,
) : CursorRemoteMediator<PlanetEntity>(lastRefreshDataStore, FEATURE_KEY) {

    override suspend fun getCount() = planetsDao.count()

    override suspend fun getNextCursor(lastItem: PlanetEntity) =
        planetsDao.remoteKeyById(lastItem.id)?.nextCursor

    override suspend fun fetch(pageSize: Int, cursor: String?): CursorPage<PlanetEntity> {
        val data = apolloClient.query(
            PlanetsQuery(
                first = Optional.Present(pageSize),
                after = Optional.presentIfNotNull(cursor),
            )
        ).execute().requireData()

        val allPlanets = data.allPlanets ?: error("allPlanets is null")

        val planets = allPlanets.planets
            .orEmpty()
            .filterNotNull()
            .mapNotNull { planet ->
                val name = planet.name ?: return@mapNotNull null
                PlanetEntity(
                    id = planet.id,
                    name = name,
                    filmCount = planet.filmConnection?.totalCount ?: 0,
                )
            }

        val nextCursor = allPlanets.pageInfo
            .takeIf { it.hasNextPage }
            ?.endCursor

        return CursorPage(items = planets, nextCursor = nextCursor)
    }

    override suspend fun save(items: List<PlanetEntity>, nextCursor: String?, clearFirst: Boolean) {
        val remoteKeys = items.map { PlanetRemoteKeyEntity(id = it.id, nextCursor = nextCursor) }
        planetsDao.upsert(planets = items, keys = remoteKeys, clearFirst = clearFirst)
    }

    companion object {
        private const val FEATURE_KEY = "planets"
    }

}
