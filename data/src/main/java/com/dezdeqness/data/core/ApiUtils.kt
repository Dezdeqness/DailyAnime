package com.dezdeqness.data.core

import okhttp3.Request

fun Request.withAccessToken(token: String) =
    newBuilder()
        .removeHeader(AUTHORIZATION_HEADER)
        .addHeader(AUTHORIZATION_HEADER, token)
        .build()

fun Request.withUserAgent() =
    newBuilder()
        .removeHeader(AUTHORIZATION_USER_AGENT)
        .addHeader(AUTHORIZATION_USER_AGENT, "Shikimori Android APP")
        .build()
