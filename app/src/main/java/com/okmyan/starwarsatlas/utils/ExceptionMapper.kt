package com.okmyan.starwarsatlas.utils

import com.okmyan.starwarsatlas.core.model.DataError
import com.okmyan.starwarsatlas.core.model.GraphQlException
import com.okmyan.starwarsatlas.core.network.NoInternetConnectionException
import java.net.SocketTimeoutException

fun Throwable.toDataError(): DataError = when {
    cause is NoInternetConnectionException -> DataError.NoInternetConnection
    this is SocketTimeoutException || cause is SocketTimeoutException -> DataError.Timeout
    this is GraphQlException -> DataError.GraphQl(message.orEmpty())
    else -> DataError.Unknown(this)
}
