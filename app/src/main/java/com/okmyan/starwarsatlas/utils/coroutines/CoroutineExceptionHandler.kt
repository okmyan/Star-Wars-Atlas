package com.okmyan.starwarsatlas.utils.coroutines

import kotlinx.coroutines.CoroutineExceptionHandler
import timber.log.Timber

fun coroutineExceptionHandler() = CoroutineExceptionHandler { context, throwable ->
    Timber.tag("CoroutineException").e(
        t = throwable,
        message = "Coroutine failed: $context",
    )
}
