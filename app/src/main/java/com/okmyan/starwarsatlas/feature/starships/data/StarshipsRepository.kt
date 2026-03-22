package com.okmyan.starwarsatlas.feature.starships.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.apollographql.apollo.ApolloClient
import com.okmyan.starwarsatlas.feature.starships.domain.StarshipListItem
import com.okmyan.starwarsatlas.utils.PAGE_SIZE
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StarshipsRepository @Inject constructor(
    private val apolloClient: ApolloClient,
) {

    fun getStarships(): Flow<PagingData<StarshipListItem>> = Pager(
        config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
        pagingSourceFactory = { StarshipsPagingSource(apolloClient) },
    ).flow

}
