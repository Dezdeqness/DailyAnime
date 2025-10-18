package com.dezdeqness.presentation.features.userrate.composable

import androidx.compose.foundation.background
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dezdeqness.R
import com.dezdeqness.core.ui.ReorderHapticFeedbackType
import com.dezdeqness.core.ui.rememberReorderHapticFeedback
import com.dezdeqness.core.ui.theme.AppTheme
import kotlin.math.roundToInt

@Composable
fun ScoreSlider(
    modifier: Modifier = Modifier,
    score: Long,
    onScoreChanged: (Long) -> Unit,
) {
    val haptic = rememberReorderHapticFeedback()

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomSlider(
            onValueChange = { newValue ->
                onScoreChanged(newValue.toLong())
                haptic.performHapticFeedback(ReorderHapticFeedbackType.MOVE)
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
            score = 0,
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

    Slider(
        modifier = modifier,
        value = value,
        valueRange = valueRange,
        steps = steps,
        onValueChange = { onValueChange(it) },
        thumb = {
            Label(value = value)
        },
        enabled = enabled
    )

}

@Composable
private fun Label(
    modifier: Modifier = Modifier,
    value: Float,
) {
    val data = if (value == 0.0f) "" else value.toInt().toString()
    MaterialTheme {
        Text(
            data,
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
