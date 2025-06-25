package com.dezdeqness.presentation.features.debugscreen

import com.dezdeqness.data.core.config.ConfigKeys

data class DebugConfigState(
    val configValues: Map<ConfigKeys, Any> = emptyMap(),
    val isOverrideEnabled: Boolean = false
)
