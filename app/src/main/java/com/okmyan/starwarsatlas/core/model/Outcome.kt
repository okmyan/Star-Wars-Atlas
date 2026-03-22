package com.okmyan.starwarsatlas.core.model

sealed interface Outcome<out T> {
    data class Success<T>(val value: T) : Outcome<T>
    data class Failure(val error: DataError) : Outcome<Nothing>
}
