package com.okmyan.starwarsatlas.feature.people.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.apollographql.apollo.ApolloClient
import com.okmyan.starwarsatlas.feature.people.domain.PersonListItem
import com.okmyan.starwarsatlas.utils.PAGE_SIZE
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PeopleRepository @Inject constructor(
    private val apolloClient: ApolloClient,
) {

    fun getPeople(): Flow<PagingData<PersonListItem>> = Pager(
        config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
        pagingSourceFactory = { PeoplePagingSource(apolloClient) },
    ).flow

}
