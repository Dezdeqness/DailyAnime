package com.dezdeqness.presentation.models

import com.google.common.collect.ImmutableList

data class VideoUiModel(
    val sourceUrl: String,
    val imageUrl: String,
    val name: String,
    val source: String,
) : AdapterItem()

data class VideoUiModelList(
    val list: ImmutableList<VideoUiModel>
) : AdapterItem()
