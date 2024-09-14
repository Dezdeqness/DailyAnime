package com.dezdeqness.data.provider

import android.content.Context
import java.util.Locale
import javax.inject.Inject

class LocaleProvider @Inject constructor(
    val context: Context,
) {

    fun getCurrentLocale(): Locale = context.resources.configuration.locales.get(0)

}
