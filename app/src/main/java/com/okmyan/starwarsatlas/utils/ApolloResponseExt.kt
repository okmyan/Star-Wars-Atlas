package com.okmyan.starwarsatlas.utils

import com.apollographql.apollo.api.ApolloResponse
import com.apollographql.apollo.api.Operation
import com.okmyan.starwarsatlas.core.model.GraphQlException

fun <D : Operation.Data> ApolloResponse<D>.requireData(): D {
    exception?.let { throw it }

    errors?.firstOrNull()?.let { throw GraphQlException(it.message) }

    return data ?: throw IllegalStateException("No data and no errors in Apollo response")
}
