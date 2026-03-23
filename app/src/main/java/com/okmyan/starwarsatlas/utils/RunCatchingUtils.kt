package com.okmyan.starwarsatlas.utils

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingSource.LoadResult
import androidx.paging.RemoteMediator.MediatorResult
import com.okmyan.starwarsatlas.core.model.Outcome
import kotlin.coroutines.cancellation.CancellationException

/**
 * Executes [block] and wraps the result in [Outcome.Success]. Thrown exceptions are caught
 * and mapped to [Outcome.Failure]. Respects coroutine cancellation by re-throwing [CancellationException].
 */
inline fun <T> outcomeOf(block: () -> T): Outcome<T> {
    return try {
        Outcome.Success(block())
    } catch (c: CancellationException) {
        throw c
    } catch (e: Throwable) {
        Outcome.Failure(e.toDataError())
    }
}

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

/**
 * Executes [block] and wraps the result in [MediatorResult.Success]. Thrown exceptions are caught
 * and wrapped in [MediatorResult.Error]. Respects coroutine cancellation by re-throwing [CancellationException].
 */
@OptIn(ExperimentalPagingApi::class)
inline fun mediatorResultOf(block: () -> MediatorResult.Success): MediatorResult {
    return try {
        block()
    } catch (c: CancellationException) {
        throw c
    } catch (e: Exception) {
        MediatorResult.Error(e)
    }
}
