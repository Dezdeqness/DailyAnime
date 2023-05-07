package com.dezdeqness.presentation.action

interface Action {

    data class AnimeClick(val animeId: Long) : Action

    data class VideoClick(val url: String) : Action

}
