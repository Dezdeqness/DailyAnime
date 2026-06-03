package com.dezdeqness.feature.screenshotsviewer

import com.dezdeqness.feature.screenshotsviewer.store.ScreenshotsNamespace
import com.dezdeqness.foundation.BaseStoreViewModel
import javax.inject.Inject
import money.vivid.elmslie.core.store.ElmStore

class ScreenshotsViewModel @Inject constructor(
    store: ElmStore<
        ScreenshotsNamespace.Event,
        ScreenshotsNamespace.State,
        ScreenshotsNamespace.Effect,
        ScreenshotsNamespace.Command,
        >,
) : BaseStoreViewModel<
    ScreenshotsNamespace.Event,
    ScreenshotsNamespace.State,
    ScreenshotsNamespace.Effect,
    ScreenshotsNamespace.Command,
    >(
    store = store,
    initialState = ScreenshotsNamespace.State(),
) {

    fun onScreenshotOpened(screenshots: List<String>, index: Int) {
        accept(
            event = ScreenshotsNamespace.Event.Initial(
                screenshots = screenshots,
                index = index,
            ),
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
