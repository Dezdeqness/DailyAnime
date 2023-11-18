package com.dezdeqness.utils

import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import javax.inject.Inject

class NetworkUtils @Inject constructor(
    private val context: Context,
) {
    fun isInternetAvailable(): Boolean {
        val connMgr =
            context.getSystemService(AppCompatActivity.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connMgr.activeNetworkInfo
        if (activeNetworkInfo != null) { // connected to the internet
            return if (activeNetworkInfo.type == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                true
            } else activeNetworkInfo.type == ConnectivityManager.TYPE_MOBILE
        }
        return false
    }

}
