package com.dezdeqness.di.source.anilist

import com.dezdeqness.data.analytics.AnalyticsManager
import com.dezdeqness.data.analytics.model.AuthStatus

class AnilistAnalyticsManager : AnalyticsManager {
    override fun personalListTracked() = Unit

    override fun homeTracked() = Unit

    override fun calendarTracked(isFromHome: Boolean) = Unit

    override fun searchTracked() = Unit

    override fun profileTracked() = Unit

    override fun detailsTracked(id: String, title: String) = Unit

    override fun settingsTracked() = Unit

    override fun authTracked(isLogin: Boolean) = Unit

    override fun authStatusTracked(status: AuthStatus) = Unit
}
