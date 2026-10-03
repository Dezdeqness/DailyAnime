package com.dezdeqness.presentation.features.debugscreen

import com.dezdeqness.contract.source.SourceType
import com.dezdeqness.data.core.config.ConfigKeys
import com.dezdeqness.presentation.features.debugscreen.page.DebugPage

interface DebugScreenActions {
    fun onInitialLoading()
    fun onOverrideConfigKeysClicked(value: Boolean)
    fun setValue(key: ConfigKeys, value: Any)
    fun onBackPressed()
    fun onApplyChangesClicked()
    fun onTriggerOnboardingClicked()
    fun onDebugPageClicked(page: DebugPage)
    fun onSourceSelected(sourceType: SourceType)
}
