package com.okmyan.starwarsatlas.feature.planets.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.apollographql.apollo.ApolloClient
import com.okmyan.starwarsatlas.feature.planets.domain.PlanetListItem
import com.okmyan.starwarsatlas.utils.PAGE_SIZE
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PlanetsRepository @Inject constructor(
    private val apolloClient: ApolloClient,
) {

    fun getPlanets(): Flow<PagingData<PlanetListItem>> = Pager(
        config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
        pagingSourceFactory = { PlanetsPagingSource(apolloClient) },
    ).flow

}
