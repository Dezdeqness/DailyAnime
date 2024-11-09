package com.dezdeqness.presentation.action

interface Action {

    data class AnimeClick(val animeId: Long) : Action

    data class VideoClick(val url: String) : Action

    data class EditRateClicked(val editRateId: Long) : Action

    data class UserRateIncrement(val editRateId: Long) : Action
}
