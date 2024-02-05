package com.dezdeqness.presentation.features.screenshotsviewer

data class ScreenshotViewerState(
    val screenshots: List<String> = listOf(),
    val currentIndex: Int = 0,
)
