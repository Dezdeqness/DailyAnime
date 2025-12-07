package com.dezdeqness.contract.settings.models

import com.dezdeqness.contract.settings.core.SettingsPreference
import com.dezdeqness.contract.settings.core.handlers.ThemeModeHandler

data object NightThemePreference : SettingsPreference<ThemeMode> {
    override val name = "appTheme"
    override val default = ThemeMode.SYSTEM
    override val handler = ThemeModeHandler
}

enum class ThemeMode(val id: Int) {
    LIGHT(1),
    DARK(2),
    AMOLED(3),
    SYSTEM(0);

    companion object {
        fun fromId(id: Int?): ThemeMode? = ThemeMode.entries.find { it.id == id }
    }
}
