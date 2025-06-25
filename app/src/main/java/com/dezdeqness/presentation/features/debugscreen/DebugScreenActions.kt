package com.dezdeqness.presentation.features.debugscreen

import com.dezdeqness.data.core.config.ConfigKeys

interface DebugScreenActions {
    fun onInitialLoading()
    fun onOverrideConfigKeysClicked(value: Boolean)
    fun setValue(key: ConfigKeys, value: Any)
}
