package com.dezdeqness.foundation.ui.utils

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowInsetsControllerCompat

@Composable
fun rememberSystemUiController(): WindowInsetsControllerCompat? {
    val view = LocalView.current
    val window = (view.context as? Activity)?.window

    return remember(view) {
        window?.let { WindowInsetsControllerCompat(it, view) }
    }
}
