package com.dezdeqness.presentation.features.animedetails

import com.dezdeqness.presentation.action.Action

interface DetailsActions {
    fun onBackPressed()
    fun onSharePressed()
    fun onFabClicked()
    fun onActionReceive(action: Action)
    fun onRetryClicked()
}
