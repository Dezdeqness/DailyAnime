package com.dezdeqness.feature.screenshotsviewer

import com.dezdeqness.foundation.BaseStoreViewModel
import com.dezdeqness.feature.screenshotsviewer.store.ScreenshotsNamespace
import money.vivid.elmslie.core.store.ElmStore
import javax.inject.Inject

class ScreenshotsViewModel @Inject constructor(
    store: ElmStore<ScreenshotsNamespace.Event, ScreenshotsNamespace.State, ScreenshotsNamespace.Effect, ScreenshotsNamespace.Command>,
) : BaseStoreViewModel<ScreenshotsNamespace.Event, ScreenshotsNamespace.State, ScreenshotsNamespace.Effect, ScreenshotsNamespace.Command>(
    store = store,
    initialState = ScreenshotsNamespace.State(),
) {

    fun onScreenshotOpened(screenshots: List<String>, index: Int) {
        accept(
            event = ScreenshotsNamespace.Event.Initial(
                screenshots = screenshots,
                index = index,
            )
        )
    }

    fun onShareButtonClicked() {
        accept(event = ScreenshotsNamespace.Event.ShareUrlClicked)
    }

    fun onScreenShotChanged(index: Int) {
        accept(event = ScreenshotsNamespace.Event.IndexChanged(index))
    }

    fun onDownloadButtonClicked() {
        accept(event = ScreenshotsNamespace.Event.DownloadClicked)
    }
}
