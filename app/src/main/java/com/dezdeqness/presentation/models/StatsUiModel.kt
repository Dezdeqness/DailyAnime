package com.dezdeqness.presentation.models

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.google.common.collect.ImmutableList

class DiagramChartUiModel(
    val maxProgress: Int,
    val items: ImmutableList<StatsData>
) : AdapterItem()

class ScoreChartUiModel(
    val maxProgress: Int,
    val items: ImmutableList<StatsData>
) : AdapterItem()

class StatsChartUiModel(
    val maxProgress: Int,
    val items: ImmutableList<StatsData>
) : AdapterItem()

data class StatsData(
    @StringRes val name: Int,
    val textName: String,
    val value: String,
    val currentProgress: Int,
    val color: Color = Color.Transparent,
)
