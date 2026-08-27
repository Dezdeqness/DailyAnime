package com.dezdeqness.data.analytics

import com.dezdeqness.data.analytics.model.AuthStatus

interface AnalyticsManager {
    fun personalListTracked()
    fun homeTracked()
    fun calendarTracked(isFromHome: Boolean = false)
    fun searchTracked()
    fun profileTracked()
    fun detailsTracked(id: String, title: String)
    fun settingsTracked()
    fun authTracked(isLogin: Boolean = true)

    fun authStatusTracked(status: AuthStatus)
}
