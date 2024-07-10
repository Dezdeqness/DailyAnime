package com.dezdeqness.data.core

import okhttp3.Interceptor
import javax.inject.Inject

class UserAgentTokenInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain) =
        chain.proceed(
            chain
                .request()
                .withUserAgent()
        )
}
