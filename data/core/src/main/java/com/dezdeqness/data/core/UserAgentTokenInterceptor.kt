package com.dezdeqness.data.core

import javax.inject.Inject
import okhttp3.Interceptor

class UserAgentTokenInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain) = chain.proceed(
        chain
            .request()
            .withUserAgent(),
    )
}
