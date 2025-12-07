package com.dezdeqness.data.manager

import androidx.appcompat.app.AppCompatDelegate
import com.dezdeqness.contract.settings.models.ThemeMode
import javax.inject.Inject

class AppThemeManager @Inject constructor() {

    fun switchTheme(mode: ThemeMode) {
        val themeMode = when (mode) {
            ThemeMode.SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            ThemeMode.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
            ThemeMode.DARK, ThemeMode.AMOLED -> AppCompatDelegate.MODE_NIGHT_YES
        }
        AppCompatDelegate.setDefaultNightMode(themeMode)
    }

}
