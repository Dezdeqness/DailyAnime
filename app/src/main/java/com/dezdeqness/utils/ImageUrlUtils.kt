package com.dezdeqness.utils

import javax.inject.Inject

class ImageUrlUtils @Inject constructor() {

    fun getImageWithBaseUrl(url: String) =
        BASE_URL + url

    fun getSecurityUrl(url: String) =
        url.replace("http", "https")


    companion object {
        private const val BASE_URL = "https://shikimori.me/"
    }

}
