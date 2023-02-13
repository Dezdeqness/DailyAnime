package com.dezdeqness.core

import android.util.Log

class AppLogger {

    fun logInfo(tag: String, message: String) {
        Log.e(tag, message)
    }

    fun logInfo(tag: String, message: String, throwable: Throwable) {
        Log.e(tag, message, throwable)
    }


}
