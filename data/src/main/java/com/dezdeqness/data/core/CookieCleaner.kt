package com.dezdeqness.data.core

import android.webkit.CookieManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CookieCleaner @Inject constructor() {

    fun clear() {
        val cookieManager = CookieManager.getInstance()
        cookieManager.removeAllCookies(null)
        cookieManager.flush()
    }

}
