package com.dezdeqness.utils

import java.util.Locale
import javax.inject.Inject

class LocaleUtils @Inject constructor() {

    fun isNonRusLocale(locale: Locale) = locale.language != LANGUAGE_RU

    companion object {
        private const val LANGUAGE_RU = "ru"
    }
}
