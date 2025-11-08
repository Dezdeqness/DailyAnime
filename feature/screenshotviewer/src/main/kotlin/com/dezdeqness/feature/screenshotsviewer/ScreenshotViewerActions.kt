package com.dezdeqness.feature.screenshotsviewer

interface ScreenshotViewerActions {
    fun onShareButtonClicked()
    fun onDownloadButtonClicked()
    fun onScreenShotChanged(index: Int)
    fun onBackClicked()
}
