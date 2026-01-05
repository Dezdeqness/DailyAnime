package com.dezdeqness.shared.presentation.bridge

import androidx.annotation.DrawableRes
import com.dezdeqness.contract.settings.repository.SettingsRepository
import com.dezdeqness.shared.presentation.provider.PermissionCheckProvider

interface ApplicationBridge {
    fun getSettingsRepository(): SettingsRepository
    fun getPermissionCheckProvider(): PermissionCheckProvider
    @DrawableRes
    fun getAppForegroundIcon(): Int

    fun getVersionName(): String
}
