package com.dezdeqness.data.core

import com.dezdeqness.contract.auth.usecase.RefreshTokenUseCase
import dagger.Lazy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class RefreshTokenInterceptor @Inject constructor(
    private val refreshTokenUseCase: Lazy<RefreshTokenUseCase>,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        val token: String

        if (request.header(AUTHORIZATION_HEADER) != null) {
            synchronized(this) {
                val tokenResult = runBlocking(Dispatchers.IO) {
                    refreshTokenUseCase.get().invoke()
                }

                if (tokenResult.isFailure) {
                    throw RefreshTokenExpiredException()
                }

                token = tokenResult.getOrDefault("")
            }

            if (token.isNotEmpty()) {
                request = request.withAccessToken("$AUTHORIZATION_BEARER$token")
            }
        }

        return chain.proceed(request)
    }

}

class RefreshTokenExpiredException : Throwable()
