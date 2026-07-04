package com.dezdeqness.feature.stats.presentation.preview

import androidx.compose.ui.graphics.Color
import com.dezdeqness.feature.stats.R
import com.dezdeqness.feature.stats.presentation.models.DiagramChartUiModel
import com.dezdeqness.feature.stats.presentation.models.ScoreChartUiModel
import com.dezdeqness.feature.stats.presentation.models.StatsData
import com.dezdeqness.feature.stats.presentation.models.StatsHeaderUiModel
import com.dezdeqness.feature.stats.presentation.models.StatsItem

object StatsPreviewData {

    private val scores = listOf(
        StatsData(name = 0, textName = "10", value = "154", currentProgress = 154),
        StatsData(name = 0, textName = "9", value = "89", currentProgress = 89),
        StatsData(name = 0, textName = "8", value = "42", currentProgress = 42),
        StatsData(name = 0, textName = "7", value = "15", currentProgress = 15),
    )

    private val statuses = listOf(
        StatsData(
            name = R.string.stats_status_watching,
            textName = "watching",
            value = "12",
            currentProgress = 12,
            color = Color(0xFF42A5F5),
        ),
        StatsData(
            name = R.string.stats_status_completed,
            textName = "completed",
            value = "230",
            currentProgress = 230,
            color = Color(0xFF66BB6A),
        ),
        StatsData(
            name = R.string.stats_status_planned,
            textName = "planned",
            value = "58",
            currentProgress = 58,
            color = Color(0xFFFFA726),
        ),
    )

    val items: List<StatsItem> = listOf(
        StatsHeaderUiModel(header = "Scores"),
        ScoreChartUiModel(
            maxProgress = scores.sumOf { it.currentProgress },
            items = scores,
        ),
        StatsHeaderUiModel(header = "Lists"),
        DiagramChartUiModel(
            maxProgress = statuses.sumOf { it.currentProgress },
            items = statuses,
        ),
    )
}
