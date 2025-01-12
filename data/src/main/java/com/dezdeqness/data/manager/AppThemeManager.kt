package com.dezdeqness.data.manager

import android.app.UiModeManager.MODE_NIGHT_NO
import android.app.UiModeManager.MODE_NIGHT_YES
import androidx.appcompat.app.AppCompatDelegate
import javax.inject.Inject

class AppThemeManager @Inject constructor() {

    fun switchTheme(isDarkTheme: Boolean) {
        val themeMode = if (isDarkTheme) {
            MODE_NIGHT_YES
        } else {
            MODE_NIGHT_NO
        }
        AppCompatDelegate.setDefaultNightMode(themeMode)
    }

}
