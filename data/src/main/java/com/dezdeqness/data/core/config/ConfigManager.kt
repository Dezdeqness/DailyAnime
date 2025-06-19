package com.dezdeqness.data.core.config

import com.dezdeqness.data.core.AppLogger
import com.dezdeqness.data.core.config.local.DebugConfigProvider
import com.dezdeqness.data.core.config.remote.RemoteConfigProvider

class ConfigManager(
    private val configSettingsProvider: ConfigSettingsProvider,
    private val remoteConfigProvider: RemoteConfigProvider,
    private val debugConfigProvider: DebugConfigProvider,
    private val appLogger: AppLogger,
) {
    val baseUrl: String
        get() = getValue(ConfigKeys.BASE_URL)

    val selectGenresCounter: Int
        get() = getValue(ConfigKeys.SELECT_GENRES_COUNTER)

    val baseGraphqlUrl: String
        get() = getValue(ConfigKeys.BASE_SHIKIMORI_GRAPHQL_URL)

    val isCalendarEnabled: Boolean
        get() = getValue<Boolean>(ConfigKeys.CALENDAR_ENABLED) == true

    suspend fun invalidate() {
        remoteConfigProvider.setDefaults()
        remoteConfigProvider.refresh()
    }

    private fun <T> getValue(key: ConfigKeys) =
        if (configSettingsProvider.isOverrideRemoteEnabled()) {
            getValue<T>(debugConfigProvider, key)
        } else {
            getValue<T>(remoteConfigProvider, key)
        }

    @Suppress("UNCHECKED_CAST")
    private fun <T> getValue(provider: BaseConfigProvider, key: ConfigKeys) =
        try {
            when (key.defaultValue) {
                is String -> provider.getStringValue(key.key) ?: key.defaultValue
                is Int -> provider.getIntValue(key.key)
                is Double -> provider.getDoubleValue(key.key)
                is Boolean -> provider.getBooleanValue(key.key) ?: key.defaultValue
                else -> key.defaultValue
            } as T
        } catch (exception: Exception) {
            appLogger.logInfo(
                TAG,
                "Provider: ${provider.javaClass.simpleName}. Error while reading key = ${key.key}: ${exception.message}"
            )

            key.defaultValue as T
        }

    companion object {
        private const val TAG = "ConfigManager"
    }

}
