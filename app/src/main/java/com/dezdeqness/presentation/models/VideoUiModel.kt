package com.dezdeqness.presentation.models

data class VideoUiModel(
    val sourceUrl: String,
    val imageUrl: String,
    val name: String,
    val source: String,
) : AdapterItem()

data class VideoUiModelList(
    val list: List<VideoUiModel>
) : AdapterItem()
