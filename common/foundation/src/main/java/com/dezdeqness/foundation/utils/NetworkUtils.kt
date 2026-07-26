package com.dezdeqness.foundation.utils

import android.content.Context
import android.net.ConnectivityManager
import javax.inject.Inject

class NetworkUtils @Inject constructor(
    private val context: Context,
) {
    fun isInternetAvailable(): Boolean {
        val manager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = manager.activeNetworkInfo
        if (activeNetworkInfo != null) {
            return activeNetworkInfo.type == ConnectivityManager.TYPE_WIFI ||
                activeNetworkInfo.type == ConnectivityManager.TYPE_MOBILE
        }
        return false
    }
}
