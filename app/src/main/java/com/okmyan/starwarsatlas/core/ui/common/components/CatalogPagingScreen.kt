package com.okmyan.starwarsatlas.core.ui.common.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.okmyan.starwarsatlas.utils.toDataError

@Composable
fun <T : Any> CatalogPagingScreen(
    pagingItems: LazyPagingItems<T>,
    key: (T) -> String,
    listState: LazyListState = rememberLazyListState(),
    itemContent: @Composable (T) -> Unit,
) {
    when (val refresh = pagingItems.loadState.refresh) {
        is LoadState.Loading -> Loading()

        is LoadState.Error -> ErrorContent(
            error = refresh.error.toDataError(),
            onRetry = { pagingItems.retry() },
        )

        is LoadState.NotLoading -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = listState,
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(
                    count = pagingItems.itemCount,
                    key = pagingItems.itemKey(key),
                ) { index ->
                    pagingItems[index]?.let { item ->
                        itemContent(item)
                    }
                }

                when (val append = pagingItems.loadState.append) {
                    is LoadState.Loading -> item {
                        Loading(modifier = Modifier.fillMaxWidth().heightIn(min = 130.dp))
                    }

                    is LoadState.Error -> item {
                        ErrorContent(
                            error = append.error.toDataError(),
                            onRetry = { pagingItems.retry() },
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }

                    is LoadState.NotLoading -> Unit
                }
            }
        }
    }
}
