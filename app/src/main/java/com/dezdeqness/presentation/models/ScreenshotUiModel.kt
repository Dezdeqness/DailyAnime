package com.dezdeqness.presentation.models

data class ScreenshotUiModel(
    val url: String,
) : AdapterItem()

data class ScreenshotUiModelList(
    val list: List<ScreenshotUiModel>,
) : AdapterItem()
