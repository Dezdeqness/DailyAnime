package com.dezdeqness.utils

import com.dezdeqness.data.core.config.ConfigManager
import javax.inject.Inject

class ImageUrlUtils @Inject constructor(
    private val configManager: ConfigManager,
) {

    fun getImageWithBaseUrl(url: String) =
        configManager.baseUrl + url

    fun getSecurityUrl(url: String) =
        url.replace("http", "https")

}
