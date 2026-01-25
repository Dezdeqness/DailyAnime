package com.dezdeqness.feature.personallist.tab

import com.dezdeqness.feature.userrate.EditRateUiModel

interface PersonalListActions {
    fun onPullDownRefreshed()
    fun onScrolled()
    fun onAnimeClicked(animeId: Long, displayName: String)
    fun onOpenEditRateClicked(editRateId: Long, displayName: String)
    fun onUserRateIncrement(editRateId: Long)
    fun onUserRateChanged(userRate: EditRateUiModel)
}

sealed interface PersonalListAction {
    data class AnimeClick(val animeId: Long, val title: String = "") : PersonalListAction

    data class EditRateClicked(val editRateId: Long, val displayName: String) : PersonalListAction

    data class UserRateIncrement(val editRateId: Long) : PersonalListAction
}
