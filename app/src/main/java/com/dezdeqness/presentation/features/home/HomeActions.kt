package com.dezdeqness.presentation.features.home

import com.dezdeqness.presentation.action.Action

interface HomeActions {
    fun onInitialLoad()
    fun onActionReceived(action: Action)
}
