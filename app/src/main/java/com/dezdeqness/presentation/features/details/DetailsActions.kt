package com.dezdeqness.presentation.features.details

import com.dezdeqness.presentation.action.Action
import com.dezdeqness.presentation.features.userrate.EditRateUiModel

interface DetailsActions {
    fun onBackPressed()
    fun onSharePressed()
    fun onFabClicked()
    fun onActionReceive(action: Action)
    fun onRetryClicked()
    fun onUserRateChanged(userRate: EditRateUiModel)
    fun onUserRateBottomDialogClosed()
}
