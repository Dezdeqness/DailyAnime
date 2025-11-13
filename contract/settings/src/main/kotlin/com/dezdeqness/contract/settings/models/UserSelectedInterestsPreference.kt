package com.dezdeqness.contract.settings.models

import com.dezdeqness.contract.settings.core.SettingsPreference
import com.dezdeqness.contract.settings.core.handlers.StringListHandler

data object UserSelectedInterestsPreference : SettingsPreference<List<String>> {
    override val name = "selectedInterests"
    override val default = listOf<String>()
    override val handler = StringListHandler
}
