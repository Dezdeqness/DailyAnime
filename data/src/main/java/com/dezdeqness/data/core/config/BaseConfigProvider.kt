package com.dezdeqness.data.core.config

abstract class BaseConfigProvider {
    abstract suspend fun refresh()
    abstract suspend fun setDefaults()

    internal abstract fun getStringValue(key: String): String?
    internal abstract fun getIntValue(key: String): Int?
    internal abstract fun getDoubleValue(key: String): Double?
    internal abstract fun getBooleanValue(key: String): Boolean?
}
