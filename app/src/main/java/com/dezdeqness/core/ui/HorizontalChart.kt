package com.dezdeqness.core.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.presentation.models.StatsData
import com.google.common.collect.ImmutableList
import kotlinx.coroutines.delay

@Composable
fun HorizontalChart(
    modifier: Modifier = Modifier,
    maxProgress: Int,
    items: ImmutableList<StatsData>
) {
    Column(modifier = modifier) {
        items.forEachIndexed { index, item ->
            val name = item.name
            val progress = item.currentProgress
            val value = item.value

            val animatedProgress = remember { Animatable(0f) }

            LaunchedEffect(Unit) {
                delay(index * 80L)
                animatedProgress.animateTo(
                    targetValue = progress / maxProgress.toFloat(),
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
            }

            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = name,
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
                        .background(AppTheme.colors.onSurface.copy(alpha = 0.15f))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(fraction = animatedProgress.value)
                            .background(AppTheme.colors.accent)
                    )
                }

            }
        }
    }
}
