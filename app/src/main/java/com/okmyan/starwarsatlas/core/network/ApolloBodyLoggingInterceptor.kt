package com.okmyan.starwarsatlas.core.network

import com.apollographql.apollo.api.http.HttpRequest
import com.apollographql.apollo.api.http.HttpResponse
import com.apollographql.apollo.network.http.HttpInterceptor
import com.apollographql.apollo.network.http.HttpInterceptorChain
import okio.Buffer
import timber.log.Timber

class ApolloBodyLoggingInterceptor : HttpInterceptor {

    override suspend fun intercept(
        request: HttpRequest,
        chain: HttpInterceptorChain,
    ): HttpResponse {
        Timber.tag("APOLLO").d("--> ${request.method.name} ${request.url}")

        request.body?.let { body ->
            val buffer = Buffer()
            body.writeTo(buffer)
            Timber.tag("APOLLO").d("--> Body: ${buffer.readUtf8()}")
        }

        val response = chain.proceed(request)

        Timber.tag("APOLLO").d("<-- HTTP ${response.statusCode}")
        val responseBody = response.body ?: return response

        val buffer = Buffer()
        responseBody.readAll(buffer)
        Timber.tag("APOLLO").d("<-- Body: ${buffer.clone().readUtf8()}")

        return HttpResponse.Builder(statusCode = response.statusCode)
            .body(buffer)
            .addHeaders(response.headers)
            .build()
    }

}
