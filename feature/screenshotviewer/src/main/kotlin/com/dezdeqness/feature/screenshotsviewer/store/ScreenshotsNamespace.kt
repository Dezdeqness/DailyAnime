package com.dezdeqness.feature.screenshotsviewer.store

interface ScreenshotsNamespace {

    data class State(
        val screenshotsList: List<String>,
        val index: Int,
    )

    sealed class Event {
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
