package com.okmyan.starwarsatlas.utils

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
