package com.dezdeqness.presentation.features.debugscreen

import com.dezdeqness.contract.source.SourceType
import com.dezdeqness.data.core.config.ConfigKeys

data class DebugConfigState(
    val configValues: Map<ConfigKeys, Any> = emptyMap(),
    val isOverrideEnabled: Boolean = false,
    val isModified: Boolean = false,
    val source: SourceType = SourceType.SHIKIMORI,
    val availableSources: List<SourceType> = emptyList(),
)
