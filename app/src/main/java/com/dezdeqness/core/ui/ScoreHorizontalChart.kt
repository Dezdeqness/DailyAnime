package com.dezdeqness.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.presentation.models.ScoreChartUiModel

@Composable
fun ScoreHorizontalChart(
    modifier: Modifier = Modifier,
    scoreChart: ScoreChartUiModel,
) {
    Column(modifier = modifier) {
        scoreChart.items.forEach { item ->
            val name = item.name
            val progress = item.currentProgress
            val value = item.value

            val emoji = when (name) {
                "10" -> "😍"
                "9"-> "😁"
                "8"-> "😊"
                "7"-> "🙂"
                "6"-> "😐"
                "5"-> "😶"
                "4"-> "😕"
                "3"-> "😟"
                "2"-> "😠"
                "1"-> "😡"
                else -> "❓"
            }

            val barColor = when (name.toInt()) {
                in 8..10 -> Color.Green
                in 5..7 -> Color.Yellow
                in 1..4 -> Color.Red
                else -> Color.Gray
            }

            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "$emoji $name",
                        fontSize = 16.sp,
                        color = AppTheme.colors.textPrimary,
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = value.toString(),
                        fontSize = 14.sp,
                        textAlign = TextAlign.End,
                        color = AppTheme.colors.textPrimary,
                    )
                }

                Box(
                    modifier = Modifier
                        .height(10.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color.DarkGray)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(fraction = progress / scoreChart.maxProgress.toFloat())
                            .background(barColor)
                    )
                }

            }
        }
    }
}
