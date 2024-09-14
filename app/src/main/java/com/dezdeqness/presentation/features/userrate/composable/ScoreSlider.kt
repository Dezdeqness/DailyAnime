package com.dezdeqness.presentation.features.userrate.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dezdeqness.R
import com.dezdeqness.core.ui.theme.AppTheme
import kotlin.math.roundToInt

@Composable
fun ScoreSlider(
    modifier: Modifier = Modifier,
    score: Long,
    onScoreChanged: (Long) -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomSlider(
            onValueChange = { newValue ->
                onScoreChanged(newValue.toLong())
            },
            value = score.toFloat(),
            valueRange = 1f..10f,
        )

        Text(
            text = when (score.toInt()) {
                1 -> stringResource(R.string.anime_score_1)
                2 -> stringResource(R.string.anime_score_2)
                3 -> stringResource(R.string.anime_score_3)
                4 -> stringResource(R.string.anime_score_4)
                5 -> stringResource(R.string.anime_score_5)
                6 -> stringResource(R.string.anime_score_6)
                7 -> stringResource(R.string.anime_score_7)
                8 -> stringResource(R.string.anime_score_8)
                9 -> stringResource(R.string.anime_score_9)
                10 -> stringResource(R.string.anime_score_10)
                else -> ""
            },
            style = AppTheme.typography.titleMedium,
            color = AppTheme.colors.textPrimary,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ScoreSliderPreview() {
    AppTheme {
        ScoreSlider(
            score = 8,
            onScoreChanged = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float>,
    enabled: Boolean = true,
) {
    val itemCount = (valueRange.endInclusive - valueRange.start).roundToInt()
    val steps = itemCount - 1

    Box(modifier = modifier) {
        Layout(
            measurePolicy = customSliderMeasurePolicy(
                itemCount = itemCount,
                value = value,
                startValue = valueRange.start
            ),
            content = {
                Label(
                    modifier = Modifier.layoutId(CustomSliderComponents.LABEL),
                    value = value,
                )

                Box(modifier = Modifier.layoutId(CustomSliderComponents.THUMB)) {
                    Thumb()
                }

                Slider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .layoutId(CustomSliderComponents.SLIDER),
                    value = value,
                    valueRange = valueRange,
                    steps = steps,
                    onValueChange = { onValueChange(it) },
                    thumb = {
                        Thumb()
                    },
                    enabled = enabled
                )

                Indicator(
                    modifier = Modifier.layoutId(CustomSliderComponents.INDICATOR),
                    valueRange = valueRange,
                    gap = itemCount
                )
            },
        )
    }
}

@Composable
private fun Label(
    modifier: Modifier = Modifier,
    value: Float,
) {
    MaterialTheme {
        Text(
            value.toInt().toString(),
            style = AppTheme.typography.titleSmall.copy(fontWeight = FontWeight.Normal),
            textAlign = TextAlign.Center,
            color = Color.White,
            modifier = modifier
                .defaultMinSize(minWidth = 25.dp, minHeight = 25.dp)
                .clip(CircleShape)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = CircleShape
                )
                .padding(4.dp)
        )
    }
}

private fun customSliderMeasurePolicy(
    itemCount: Int,
    value: Float,
    startValue: Float
) = MeasurePolicy { measurables, constraints ->
    val thumbPlaceable = measurables.first {
        it.layoutId == CustomSliderComponents.THUMB
    }.measure(constraints)
    val thumbRadius = (thumbPlaceable.width / 2).toFloat()

    val indicatorPlaceables = measurables.filter {
        it.layoutId == CustomSliderComponents.INDICATOR
    }.map { measurable ->
        measurable.measure(constraints)
    }
    val indicatorHeight = indicatorPlaceables.maxByOrNull { it.height }?.height ?: 0

    val sliderPlaceable = measurables.first {
        it.layoutId == CustomSliderComponents.SLIDER
    }.measure(constraints)
    val sliderHeight = sliderPlaceable.height

    val labelPlaceable = measurables.find {
        it.layoutId == CustomSliderComponents.LABEL
    }?.measure(constraints)
    val labelHeight = labelPlaceable?.height ?: 0

    val width = sliderPlaceable.width
    val height = labelHeight + sliderHeight + indicatorHeight

    val trackWidth = width - (2 * thumbRadius)

    val sectionWidth = trackWidth / itemCount
    val indicatorSpacing = sectionWidth * (itemCount)

    val labelOffset = (sectionWidth * (value - startValue)) + thumbRadius

    layout(width = width, height = height) {
        var indicatorOffsetX = thumbRadius

        labelPlaceable?.placeRelative(
            x = (labelOffset - (labelPlaceable.width / 2)).roundToInt(),
            y = 0
        )
        sliderPlaceable.placeRelative(x = 0, y = labelHeight - (sliderHeight * 0.25f).toInt())

        indicatorPlaceables.forEach { placeable ->
            placeable.placeRelative(
                x = (indicatorOffsetX - (placeable.width / 2)).roundToInt(),
                y = labelHeight + sliderHeight
            )
            indicatorOffsetX += indicatorSpacing
        }
    }
}

@Composable
fun Thumb(
    modifier: Modifier = Modifier,
    size: Dp = ThumbSize,
    shape: Shape = CircleShape,
) {
    Box(
        modifier = modifier
            .defaultMinSize(minWidth = size, minHeight = size)
            .clip(shape)
            .background(colorResource(id = R.color.purple_200))
    )
}


@Composable
fun Indicator(
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float>,
    gap: Int,
) {
    for (i in valueRange.start.roundToInt()..valueRange.endInclusive.roundToInt() step gap) {
        Box(modifier = modifier) {
            Text(
                text = i.toString(),
                style = AppTheme.typography.titleSmall.copy(fontSize = 14.sp),
                textAlign = TextAlign.Center,
                color = AppTheme.colors.textPrimary,
            )
        }
    }
}

private enum class CustomSliderComponents {
    SLIDER, LABEL, INDICATOR, THUMB
}

private val ThumbSize = 20.dp
