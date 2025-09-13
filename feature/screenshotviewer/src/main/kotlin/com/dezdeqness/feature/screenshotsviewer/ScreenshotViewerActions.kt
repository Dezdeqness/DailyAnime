package com.dezdeqness.feature.screenshotsviewer

interface ScreenshotViewerActions {
    fun onShareButtonClicked()
    fun onShowSystemUi()
    fun onHideSystemUi()
    fun onScreenShotChanged(index: Int)
    fun onBackClicked()
}
