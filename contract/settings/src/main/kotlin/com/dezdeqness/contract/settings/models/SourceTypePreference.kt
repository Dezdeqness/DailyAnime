package com.dezdeqness.contract.settings.models

import com.dezdeqness.contract.settings.core.SettingsPreference
import com.dezdeqness.contract.settings.core.handlers.SourceTypeHandler
import com.dezdeqness.contract.source.SourceType

data object SourceTypePreference : SettingsPreference<SourceType> {
    override val name = "sourceType"
    override val default = SourceType.SHIKIMORI
    override val handler = SourceTypeHandler
}
