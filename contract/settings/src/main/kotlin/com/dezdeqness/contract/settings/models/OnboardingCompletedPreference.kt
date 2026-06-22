package com.dezdeqness.contract.settings.models

import com.dezdeqness.contract.settings.core.SettingsPreference
import com.dezdeqness.contract.settings.core.handlers.BooleanHandler

data object OnboardingCompletedPreference : SettingsPreference<Boolean> {
    override val name = "onboardingCompleted"
    override val default = false
    override val handler = BooleanHandler
}
