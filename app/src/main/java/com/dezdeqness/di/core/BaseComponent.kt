package com.dezdeqness.di.core

import androidx.lifecycle.ViewModelProvider
import com.dezdeqness.data.analytics.AnalyticsManager
import com.dezdeqness.presentation.routing.ApplicationRouter

interface BaseComponent {
    fun viewModelFactory(): ViewModelProvider.Factory

    fun applicationRouter(): ApplicationRouter

    fun analyticsManager(): AnalyticsManager
}
