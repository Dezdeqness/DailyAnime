package com.dezdeqness.data.analytics.model

import android.os.Bundle
import androidx.core.os.bundleOf

data class AnalyticsEvent(
    val screenName: AnalyticsScreenName,
    val data: Bundle = bundleOf(),
)
