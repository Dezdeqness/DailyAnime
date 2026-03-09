package com.dezdeqness.foundation.message

interface BaseMessageProvider {
    fun getAnimeEditCreateSuccessMessage(): String
    fun getAnimeEditUpdateSuccessMessage(): String
    fun getAnimeEditRateErrorMessage(): String
    fun getGeneralErrorMessage(): String
}
