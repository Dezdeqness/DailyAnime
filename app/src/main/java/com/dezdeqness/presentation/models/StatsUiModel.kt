package com.dezdeqness.presentation.models

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
    val name: String,
    val value: String,
    val currentProgress: Int,
)
