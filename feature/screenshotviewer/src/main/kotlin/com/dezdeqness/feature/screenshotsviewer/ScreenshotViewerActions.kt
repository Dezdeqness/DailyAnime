package com.dezdeqness.feature.screenshotsviewer

interface ScreenshotViewerActions {
    fun onShareButtonClicked()
    fun onDownloadButtonClicked()
    fun onShowSystemUi()
    fun onHideSystemUi()
    fun onScreenShotChanged(index: Int)
    fun onBackClicked()
}
