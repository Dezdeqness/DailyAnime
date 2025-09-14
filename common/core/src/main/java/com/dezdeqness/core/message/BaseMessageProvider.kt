package com.dezdeqness.core.message

interface BaseMessageProvider {
    fun getAnimeEditCreateSuccessMessage(): String
    fun getAnimeEditUpdateSuccessMessage(): String
    fun getAnimeEditRateErrorMessage(): String
    fun getGeneralErrorMessage(): String
}
