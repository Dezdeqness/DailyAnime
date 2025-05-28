package com.dezdeqness.presentation.action

interface Action {

    data class AnimeClick(val animeId: Long, val title: String = "") : Action

    data class CharacterClick(val characterId: Long) : Action

    data class VideoClick(val url: String) : Action

    data class EditRateClicked(val editRateId: Long) : Action

    data class UserRateIncrement(val editRateId: Long) : Action

    data object CalendarHeaderClicked : Action

    data object SimilarClicked : Action

    data object StatsClicked : Action

    data object ChronologyClicked : Action

    data class ScreenShotClick(val screenshotUrl: String) : Action
}
