package com.dezdeqness.presentation.features.screenshotsviewer.store

import com.dezdeqness.data.BuildConfig
import com.dezdeqness.pod.core.store.Reducer
import com.dezdeqness.pod.core.store.ReducerResultBuilder
import com.dezdeqness.presentation.features.screenshotsviewer.store.ScreenshotsNamespace.Effect
import com.dezdeqness.presentation.features.screenshotsviewer.store.ScreenshotsNamespace.Event
import com.dezdeqness.presentation.features.screenshotsviewer.store.ScreenshotsNamespace.State

val screenshotReducer = object : Reducer<Event, State, Effect>() {

    override fun ReducerResultBuilder<State, Effect>.reduce(event: Event) {
        when (event) {
            is Event.Init -> {
                effects {
                    Effect.ScrollToPage(index = state.index)
                }
            }

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
                effects {
                    Effect.ShareUrl(url = formattedUrl)
                }
            }
        }
    }

}
