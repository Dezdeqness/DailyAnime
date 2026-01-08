package com.dezdeqness.feature.settings.store.models

abstract class SettingUiPref {
    abstract val id: String
    abstract val sectionId: String

    val enabled: Boolean = true
}
