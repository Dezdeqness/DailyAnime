package com.dezdeqness.data.core

import com.dezdeqness.data.manager.TokenManager
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import retrofit2.Invocation
import javax.inject.Inject

class AuthorizationTokenInterceptor @Inject constructor(
    private val tokenManager: TokenManager,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        if (request.isNeedAuthorization()) {
            val tokenData = tokenManager.getTokenData()
            val token = tokenData.accessToken
            if (token.isNotEmpty()) {
                request = request.withAccessToken("$AUTHORIZATION_BEARER$token")
            } else {
                throw AccessTokenEmptyException()
            }
        }

        return chain.proceed(request)
    }
}

class AccessTokenEmptyException : Throwable()

private fun <T: Annotation> Request.getCustomAnnotation(annotationClass: Class<T>): T? =
    this.tag(Invocation::class.java)?.method()?.getAnnotation(annotationClass)

private fun Request.isNeedAuthorization() =
    this.getCustomAnnotation(NeedAuthorization::class.java) != null

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class NeedAuthorization
