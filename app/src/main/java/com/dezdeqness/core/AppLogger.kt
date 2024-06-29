package com.dezdeqness.core

import android.util.Log
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase

class AppLogger {

    fun logInfo(tag: String, message: String) {
        Log.e(tag, message)
        Firebase.crashlytics.log("$tag: $message")
    }

    fun logInfo(tag: String, message: String, throwable: Throwable) {
        Firebase.crashlytics.log("$tag: $message")
        Firebase.crashlytics.recordException(throwable)
        Firebase.crashlytics.sendUnsentReports()
        Log.e(tag, message, throwable)
    }

}
