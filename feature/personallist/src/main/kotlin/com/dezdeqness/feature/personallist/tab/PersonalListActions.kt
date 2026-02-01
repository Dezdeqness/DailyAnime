package com.dezdeqness.feature.personallist.tab

interface PersonalListActions {
    fun onPullDownRefreshed()
    fun onLoadMore()
    fun onAnimeClicked(animeId: Long, displayName: String)
    fun onOpenEditRateClicked(editRateId: Long, displayName: String)
    fun onUserRateIncrement(editRateId: Long)
}

sealed interface PersonalListAction {
    data class AnimeClick(val animeId: Long, val title: String = "") : PersonalListAction

    data class EditRateClicked(val editRateId: Long, val displayName: String) : PersonalListAction

    data class UserRateIncrement(val editRateId: Long) : PersonalListAction
}
