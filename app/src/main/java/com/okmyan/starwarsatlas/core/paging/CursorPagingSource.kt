package com.okmyan.starwarsatlas.core.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.apollographql.apollo.ApolloClient
import com.okmyan.starwarsatlas.utils.loadResultOf

data class CursorPage<Value : Any>(
    val items: List<Value>,
    val nextCursor: String?,
)

abstract class CursorPagingSource<Value : Any>(
    protected val apolloClient: ApolloClient,
) : PagingSource<String, Value>() {

    override fun getRefreshKey(state: PagingState<String, Value>): String? = null

    override suspend fun load(params: LoadParams<String>): LoadResult<String, Value> {
        return loadResultOf {
            val result = fetch(loadSize = params.loadSize, cursor = params.key)

            LoadResult.Page(
                data = result.items,
                prevKey = null,
                nextKey = result.nextCursor,
            )
        }
    }

    protected abstract suspend fun fetch(loadSize: Int, cursor: String?): CursorPage<Value>

}
