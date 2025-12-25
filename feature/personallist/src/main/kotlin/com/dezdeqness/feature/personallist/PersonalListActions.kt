package com.dezdeqness.feature.personallist

import com.dezdeqness.feature.userrate.EditRateUiModel

interface PersonalListActions {
    fun onPullDownRefreshed()
    fun onScrolled()
    fun onInitialLoad()

    fun onActionReceived(action: PersonalListAction)
    fun onQueryChanged(query: String)
    fun onOrderChanged(order: String)
    fun onRibbonItemSelected(id: String)
    fun onUserRateChanged(userRate: EditRateUiModel)
    fun onUserRateBottomDialogClosed()
}

sealed interface PersonalListAction {
    data class AnimeClick(val animeId: Long, val title: String = "") : PersonalListAction

    data class EditRateClicked(val editRateId: Long) : PersonalListAction

    data class UserRateIncrement(val editRateId: Long) : PersonalListAction
}
