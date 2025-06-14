package com.dezdeqness.presentation.features.screenshotsviewer.store

import com.dezdeqness.data.BuildConfig
import com.dezdeqness.presentation.features.screenshotsviewer.store.ScreenshotsNamespace.Command
import com.dezdeqness.presentation.features.screenshotsviewer.store.ScreenshotsNamespace.Effect
import com.dezdeqness.presentation.features.screenshotsviewer.store.ScreenshotsNamespace.Event
import com.dezdeqness.presentation.features.screenshotsviewer.store.ScreenshotsNamespace.State
import money.vivid.elmslie.core.store.StateReducer

val screenshotReducer = object : StateReducer<Event, State, Effect, Command>() {

    override fun Result.reduce(event: Event) {
        when (event) {
            is Event.IndexChanged -> {
                state {
                    copy(index = event.index)
                }
            }

            is Event.ShareUrlClicked -> {
                val list = state.screenshotsList
                val url = list[state.index]
                val formattedUrl = if (url.startsWith(BuildConfig.BASE_URL).not()) {
                    BuildConfig.BASE_URL + url
                } else {
                    url
                }
                effects { +Effect.ShareUrl(url = formattedUrl) }
            }
        }
    }

}
