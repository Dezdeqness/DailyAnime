package com.dezdeqness.data.core.config

import com.dezdeqness.data.core.AppLogger

class ConfigManager(
    private val configProvider: BaseConfigProvider,
    private val appLogger: AppLogger,
) {
    val baseUrl: String
        get() = getValue(ConfigKeys.BASE_URL)

    val electGenresCounter: Int
        get() = getValue(ConfigKeys.SELECT_GENRES_COUNTER)

    val baseGraphqlUrl: String
        get() = getValue(ConfigKeys.BASE_GRAPHQL_URL)

    suspend fun invalidate() {
        configProvider.setDefaults()
        configProvider.refresh()
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> getValue(key: ConfigKeys) =
        try {
            when (key.defaultValue) {
                is String -> configProvider.getStringValue(key.key) ?: key.defaultValue
                is Int -> configProvider.getIntValue(key.key)
                is Double -> configProvider.getDoubleValue(key.key)
                else -> key.defaultValue
            } as T
        } catch (exception: Exception) {
            appLogger.logInfo(TAG, "Error while reading key = ${key.key}: ${exception.message}")

            key.defaultValue as T
        }

    companion object {
        private const val TAG = "ConfigManager"
    }

}
