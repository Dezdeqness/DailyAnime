package com.dezdeqness.feature.screenshotsviewer

import androidx.lifecycle.ViewModel
import com.dezdeqness.feature.screenshotsviewer.store.ScreenshotsNamespace
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import money.vivid.elmslie.core.store.ElmStore
import javax.inject.Inject

class ScreenshotsViewModel @Inject constructor(
    private val store: ElmStore<ScreenshotsNamespace.Event, ScreenshotsNamespace.State, ScreenshotsNamespace.Effect, ScreenshotsNamespace.Command>,
) : ViewModel() {

    val state: StateFlow<ScreenshotsNamespace.State> = store.states

    val effects: Flow<ScreenshotsNamespace.Effect> = store.effects

    fun onScreenshotOpened(screenshots: List<String>, index: Int) {
        store.accept(
            event = ScreenshotsNamespace.Event.Initial(
                screenshots = screenshots,
                index = index,
            )
        )
    }

    fun onShareButtonClicked() {
        store.accept(event = ScreenshotsNamespace.Event.ShareUrlClicked)
    }

    fun onScreenShotChanged(index: Int) {
        store.accept(event = ScreenshotsNamespace.Event.IndexChanged(index))
    }
    
    fun onDownloadButtonClicked() {
        store.accept(event = ScreenshotsNamespace.Event.DownloadClicked)
    }
}
