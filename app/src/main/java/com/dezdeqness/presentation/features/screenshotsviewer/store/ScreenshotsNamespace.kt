package com.dezdeqness.presentation.features.screenshotsviewer.store

interface ScreenshotsNamespace {

    data class State(
        val screenshotsList: List<String>,
        val index: Int,
    )

    sealed class Event {
        data object Init : Event()

        data class IndexChanged(val index: Int) : Event()

        data object ShareUrlClicked : Event()

    }

    sealed class Effect {
        data class ShareUrl(val url: String) : Effect()

        data class ScrollToPage(val index: Int) : Effect()
    }

}
