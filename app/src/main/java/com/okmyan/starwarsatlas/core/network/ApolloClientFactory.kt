package com.okmyan.starwarsatlas.core.network

import android.content.Context
import com.apollographql.apollo.ApolloClient
import com.okmyan.starwarsatlas.BuildConfig

private const val SERVER_URL = "https://swapi-graphql.eskerda.vercel.app/"

object ApolloClientFactory {

    fun create(context: Context): ApolloClient {
        return ApolloClient.Builder()
            .serverUrl(SERVER_URL)
            .addHttpInterceptor(ConnectivityInterceptor(context))
            .addLoggingInterceptor()
            .build()
    }

    private fun ApolloClient.Builder.addLoggingInterceptor() = apply {
        if (BuildConfig.DEBUG) {
            addHttpInterceptor(ApolloBodyLoggingInterceptor())
        }
    }

}
