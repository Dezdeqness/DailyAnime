package com.dezdeqness.data.core

import com.dezdeqness.data.manager.TokenManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class GraphqlAuthorizationInterceptor @Inject constructor(
    private val tokenManager: TokenManager,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        val tokenData = tokenManager.getTokenData()
        val token = tokenData.accessToken

        if (token.isNotEmpty()) {
            request = request.withAccessToken("$AUTHORIZATION_BEARER$token")
        }

        return chain.proceed(request)
    }
}
