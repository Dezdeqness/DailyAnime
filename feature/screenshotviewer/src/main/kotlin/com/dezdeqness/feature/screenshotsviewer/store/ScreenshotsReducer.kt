package com.dezdeqness.feature.screenshotsviewer.store

import com.dezdeqness.data.BuildConfig
import com.dezdeqness.feature.screenshotsviewer.store.ScreenshotsNamespace.Command
import com.dezdeqness.feature.screenshotsviewer.store.ScreenshotsNamespace.Effect
import com.dezdeqness.feature.screenshotsviewer.store.ScreenshotsNamespace.Effect.DownloadImage
import com.dezdeqness.feature.screenshotsviewer.store.ScreenshotsNamespace.Effect.ShareUrl
import com.dezdeqness.feature.screenshotsviewer.store.ScreenshotsNamespace.Event
import com.dezdeqness.feature.screenshotsviewer.store.ScreenshotsNamespace.State
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
                effects { +ShareUrl(url = formattedUrl) }
            }

            is Event.DownloadClicked -> {
                val list = state.screenshotsList
                val url = list[state.index]
                val formattedUrl = if (url.startsWith(BuildConfig.BASE_URL).not()) {
                    BuildConfig.BASE_URL + url
                } else {
                    url
                }
                val fileName = "screenshot_${System.currentTimeMillis()}.jpg"
                effects { +DownloadImage(url = formattedUrl, fileName = fileName) }
            }

            is Event.Initial -> {
                state {
                    copy(
                        screenshotsList = event.screenshots,
                        index = event.index
                    )
                }
            }
        }
    }

}
