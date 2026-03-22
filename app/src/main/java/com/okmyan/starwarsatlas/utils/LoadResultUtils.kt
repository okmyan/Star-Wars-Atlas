package com.okmyan.starwarsatlas.utils

import androidx.paging.PagingSource.LoadResult
import kotlin.coroutines.cancellation.CancellationException

/**
 * Executes [block] and wraps the result in [LoadResult.Page]. Thrown exceptions are caught
 * and wrapped in [LoadResult.Error]. Respects coroutine cancellation by re-throwing [CancellationException].
 */
inline fun <Key : Any, Value : Any> loadResultOf(block: () -> LoadResult.Page<Key, Value>): LoadResult<Key, Value> {
    return try {
        block()
    } catch (c: CancellationException) {
        throw c
    } catch (e: Exception) {
        LoadResult.Error(e)
    }
}
