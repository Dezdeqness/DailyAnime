package com.dezdeqness.data.core

import android.util.Log
import com.dezdeqness.foundation.Logger
import com.google.firebase.Firebase
import com.google.firebase.crashlytics.crashlytics

class AppLogger : Logger {

    override fun logInfo(tag: String, message: String) {
        Log.e(tag, message)
        Firebase.crashlytics.log("$tag: $message")
    }

    override fun logInfo(tag: String, message: String, throwable: Throwable) {
        Firebase.crashlytics.log("$tag: $message")
        Firebase.crashlytics.recordException(throwable)
        Firebase.crashlytics.sendUnsentReports()
        Log.e(tag, message, throwable)
    }
}
