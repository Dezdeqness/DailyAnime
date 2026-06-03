package com.dezdeqness.shared.presentation.utils

import com.dezdeqness.data.core.config.ConfigManager
import javax.inject.Inject

class UrlNormalizer @Inject constructor(
    private val configManager: ConfigManager,
) {

    fun normalize(url: String): String = when {
        url.startsWith("//") -> "https:$url"
        url.startsWith("/") -> configManager.baseUrl.trimEnd('/') + url
        else -> url
    }
}
