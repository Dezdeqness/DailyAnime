package com.dezdeqness.contract.settings.models

import com.dezdeqness.contract.settings.core.SettingsPreference
import com.dezdeqness.contract.settings.core.handlers.InitialSectionHandler


data object InitialSectionPreference : SettingsPreference<InitialSection> {
    override val name = "selectedInitialSection"
    override val default = InitialSection.HOME
    override val handler = InitialSectionHandler
}

enum class InitialSection(val id: Int) {
    HOME(0),
    FAVORITES(1),
    CALENDAR(2),
    PROFILE(3),
    SEARCH(4);

    companion object {
        fun fromId(id: Int?): InitialSection? = InitialSection.entries.find { it.id == id }
    }
}
