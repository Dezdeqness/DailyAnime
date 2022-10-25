package com.dezdeqness.utils

import javax.inject.Inject

class ImageUrlUtils @Inject constructor() {

    fun parseOriginalUrl(images: Map<String, String>) =
        BASE_URL + images[QUALITY_ORIGINAL].orEmpty()

    fun getImageWithBaseUrl(preview: String) =
        BASE_URL + preview

    fun getSecurityUrl(url: String) =
        url.replace("http", "https")


    companion object {
        private const val BASE_URL = "https://shikimori.one/"
        private const val QUALITY_ORIGINAL = "original"
    }

}
