package com.dezdeqness.presentation.features.genericlistscreen

import com.dezdeqness.presentation.action.Action

interface GenericListableActions {
    fun onActionReceive(action: Action)
    fun onRetryClicked()
    fun onBackPressed()
}
