package com.dezdeqness.data.core.config

import com.dezdeqness.data.core.BuildConfig
import javax.inject.Inject

class AuthConfig @Inject constructor() {
    val clientId: String get() = BuildConfig.SHIKIMORI_CLIENT_ID
    val clientSecret: String get() = BuildConfig.SHIKIMORI_CLIENT_SECRET
    val redirectUri: String get() = BuildConfig.SHIKIMORI_REDIRECT_URI
}
