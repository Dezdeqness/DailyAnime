package com.dezdeqness.feature.screenshotsviewer.store

interface ScreenshotsNamespace {

    data class State(
        val screenshotsList: List<String> = listOf(),
        val index: Int = 0,
    )

    sealed class Event {
        data class Initial(val screenshots: List<String>, val index: Int) : Event()
        data class IndexChanged(val index: Int) : Event()
        data object ShareUrlClicked : Event()
        data object DownloadClicked : Event()
    }

    sealed class Effect {
        data class ShareUrl(val url: String) : Effect()
        data class DownloadImage(val url: String, val fileName: String) : Effect()
    }

    sealed interface Command
}
