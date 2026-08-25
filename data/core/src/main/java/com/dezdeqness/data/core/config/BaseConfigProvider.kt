package com.dezdeqness.data.core.config

interface BaseConfigProvider {
    fun getStringValue(key: String): String?
    fun getIntValue(key: String): Int?
    fun getDoubleValue(key: String): Double?
    fun getBooleanValue(key: String): Boolean?
}
