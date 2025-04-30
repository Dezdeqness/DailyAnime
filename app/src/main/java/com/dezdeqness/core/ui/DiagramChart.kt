package com.dezdeqness.core.ui

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.presentation.models.StatsData
import com.google.common.collect.ImmutableList
import kotlin.collections.map

@Composable
fun DiagramChart(
    modifier: Modifier = Modifier,
    chartData: ImmutableList<StatsData>,
    totalProgress: Int,
    progressThickness: Dp = 10.dp,
    gapDegrees: Float = 2f,
) {
    val proportions = remember(totalProgress) {
        chartData.map { it.currentProgress / totalProgress.toFloat() }
    }

    val transitionState = remember {
        MutableTransitionState(AnimatedCircleProgress.START)
            .apply { targetState = AnimatedCircleProgress.END }
    }

    val stroke = with(LocalDensity.current) { Stroke(progressThickness.toPx()) }

    val transition = rememberTransition(transitionState, label = "transition")
    val angleOffset = transition.animateFloat(
        transitionSpec = {
            tween(
                delayMillis = 200,
                durationMillis = 900,
                easing = LinearOutSlowInEasing,
            )
        },
        label = "angleOffset",
    ) { state ->
        if (state == AnimatedCircleProgress.START) 0f else 360f
    }


    Column(modifier = modifier.fillMaxWidth()) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        ) {
            val radius = (size.minDimension - stroke.width) / 4
            val center = size.center
            val arcSize = Size(radius * 2, radius * 2)
            val topLeft = Offset(center.x - radius, center.y - radius)

            var startAngle = -90f

            proportions.forEachIndexed { index, proportion ->
                val sweepAngle = proportion * angleOffset.value
                val adjustedSweep = (sweepAngle - gapDegrees).coerceAtLeast(0f)
                drawArc(
                    color = chartData.getOrNull(index)?.name?.colorByStatus() ?: Color.Gray,
                    startAngle = startAngle,
                    sweepAngle = adjustedSweep,
                    topLeft = topLeft,
                    size = arcSize,
                    useCenter = false,
                    style = stroke
                )

                startAngle += sweepAngle
            }
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            chartData.forEach { data ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(data.name.colorByStatus())
                    )
                    Text(
                        text = data.name,
                        color = AppTheme.colors.textPrimary,
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = data.value,
                        color = AppTheme.colors.textPrimary,
                    )
                }
            }
        }
    }
}

// Strong hardcode due to no proper data from server
private fun String.colorByStatus() =
    when (this.lowercase()) {
        "смотрю" -> Color(0xFF42A5F5)
        "просмотрено" -> Color(0xFF66BB6A)
        "брошено" -> Color(0xFFEF5350)
        "запланировано" -> Color(0xFFFFA726)
        "пересмотр" -> Color(0xFFAB47BC)
        "отложено" -> Color(0xFF26C6DA)
        else -> Color.Gray

    }

private enum class AnimatedCircleProgress { START, END }
