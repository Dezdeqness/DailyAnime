package com.dezdeqness.feature.stats.presentation.models

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color

sealed interface StatsItem

data class StatsHeaderUiModel(
    val header: String,
) : StatsItem

data class DiagramChartUiModel(
    val maxProgress: Int,
    val items: List<StatsData>,
) : StatsItem

data class ScoreChartUiModel(
    val maxProgress: Int,
    val items: List<StatsData>,
) : StatsItem

data class StatsChartUiModel(
    val maxProgress: Int,
    val items: List<StatsData>,
) : StatsItem

data class StatsData(
    @StringRes val name: Int,
    val textName: String,
    val value: String,
    val currentProgress: Int,
    val color: Color = Color.Transparent,
)
