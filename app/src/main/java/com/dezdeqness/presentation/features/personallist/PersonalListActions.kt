package com.dezdeqness.presentation.features.personallist

import com.dezdeqness.presentation.action.Action
import com.dezdeqness.presentation.features.userrate.EditRateUiModel

interface PersonalListActions {
    fun onPullDownRefreshed()
    fun onScrolled()
    fun onInitialLoad()
    fun onActionReceived(action: Action)
    fun onQueryChanged(query: String)
    fun onOrderChanged(order: String)
    fun onRibbonItemSelected(id: String)
    fun onUserRateChanged(userRate: EditRateUiModel)
    fun onUserRateBottomDialogClosed()
}
