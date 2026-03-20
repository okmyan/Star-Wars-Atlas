package com.okmyan.starwarsatlas.core.model

sealed interface DataError {
    data object NoInternetConnection : DataError
    data object Timeout : DataError
    data class GraphQl(val message: String) : DataError
    data class Unknown(val cause: Throwable? = null) : DataError
}
