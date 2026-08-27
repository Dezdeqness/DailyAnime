package com.dezdeqness.data.analytics.impl

import android.os.Build
import android.os.Bundle
import androidx.core.os.bundleOf
import com.dezdeqness.contract.user.repository.UserRepository
import com.dezdeqness.data.analytics.AnalyticsManager
import com.dezdeqness.data.analytics.model.AnalyticsEvent
import com.dezdeqness.data.analytics.model.AnalyticsScreenName
import com.dezdeqness.data.analytics.model.AuthStatus
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class AnalyticsManagerImpl(
    private val firebaseAnalytics: FirebaseAnalytics,
    private val userRepository: UserRepository,
) : AnalyticsManager {

    private val deviceInfo: Bundle by lazy {
        bundleOf(
            "device_model" to Build.MODEL,
            "device_name" to Build.DEVICE,
            "manufacturer" to Build.MANUFACTURER,
            "os_version" to Build.VERSION.RELEASE,
            "api_level" to Build.VERSION.SDK_INT.toString(),
        )
    }

    override fun personalListTracked() {
        val data = bundleOf().apply {
            putUserId()
            putAll(deviceInfo)
        }

        val event = AnalyticsEvent(screenName = AnalyticsScreenName.PersonalList, data = data)
        logEvent(event)
    }

    override fun homeTracked() {
        val data = bundleOf().apply {
            putUserId()
            putAll(deviceInfo)
        }

        val event = AnalyticsEvent(screenName = AnalyticsScreenName.Home, data = data)
        logEvent(event)
    }

    override fun calendarTracked(isFromHome: Boolean) {
        val data = bundleOf().apply {
            putUserId()
            putAll(deviceInfo)
            putBoolean(FROM_HOME, isFromHome)
        }

        val event = AnalyticsEvent(screenName = AnalyticsScreenName.Calendar, data = data)
        logEvent(event)
    }

    override fun searchTracked() {
        val data = bundleOf().apply {
            putUserId()
            putAll(deviceInfo)
        }

        val event = AnalyticsEvent(screenName = AnalyticsScreenName.Search, data = data)
        logEvent(event)
    }

    override fun profileTracked() {
        val data = bundleOf().apply {
            putUserId()
            putAll(deviceInfo)
        }

        val event = AnalyticsEvent(screenName = AnalyticsScreenName.Profile, data = data)
        logEvent(event)
    }

    override fun detailsTracked(id: String, title: String) {
        val data = bundleOf().apply {
            putUserId()
            putAll(deviceInfo)
            putString(ANIME_ID, id)
            putString(ANIME_TITLE, title)
        }

        val event = AnalyticsEvent(screenName = AnalyticsScreenName.Details, data = data)
        logEvent(event)
    }

    override fun settingsTracked() {
        val data = bundleOf().apply {
            putUserId()
            putAll(deviceInfo)
        }

        val event = AnalyticsEvent(screenName = AnalyticsScreenName.Settings, data = data)
        logEvent(event)
    }

    override fun authTracked(isLogin: Boolean) {
        val data = bundleOf().apply {
            putUserId()
            putAll(deviceInfo)
            putBoolean(LOGIN_FLOW, isLogin)
        }

        val event = AnalyticsEvent(screenName = AnalyticsScreenName.Auth, data = data)
        logEvent(event)
    }

    override fun authStatusTracked(status: AuthStatus) {
        val data = bundleOf().apply {
            putUserId()
            putAll(deviceInfo)
            putString(AUTH_STATUS, status.name)
        }

        val event = AnalyticsEvent(screenName = AnalyticsScreenName.Auth, data = data)
        logEvent(event)
    }

    private fun Bundle.putUserId() = this.apply {
        putString(USER_ID, getUserId())
    }

    private fun logEvent(event: AnalyticsEvent) {
        firebaseAnalytics.logEvent(event.screenName.name, event.data)
    }

    private fun getUserId() = runBlocking(Dispatchers.IO) {
        userRepository.getProfileLocal()?.id?.toString() ?: ANONYMOUS_USER
    }

    companion object {
        private const val ANONYMOUS_USER = "anonymous"
        private const val USER_ID = "userId"
        private const val FROM_HOME = "fromHome"
        private const val LOGIN_FLOW = "loginFlow"
        private const val ANIME_ID = "animeId"
        private const val ANIME_TITLE = "animeTitle"
        private const val AUTH_STATUS = "authStatus"
    }
}
