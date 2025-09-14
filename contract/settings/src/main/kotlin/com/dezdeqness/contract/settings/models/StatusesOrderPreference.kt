package com.dezdeqness.contract.settings.models

import com.dezdeqness.contract.settings.core.SettingsPreference
import com.dezdeqness.contract.settings.core.handlers.StringListHandler

data object StatusesOrderPreference : SettingsPreference<List<String>> {
    override val name = "statusOrder"
    override val default = listOf<String>()
    override val handler = StringListHandler
}
