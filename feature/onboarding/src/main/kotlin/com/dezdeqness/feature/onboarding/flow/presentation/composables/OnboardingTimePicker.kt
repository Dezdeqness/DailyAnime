package com.dezdeqness.feature.onboarding.flow.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dezdeqness.foundation.ui.theme.AppTheme
import java.util.Locale

private const val HOURS_IN_DAY = 24
private const val MINUTES_IN_HOUR = 60
private const val MINUTE_STEP = 5

@Composable
fun OnboardingTimePicker(
    hours: Int,
    minutes: Int,
    onTimeChanged: (hours: Int, minutes: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TimeColumn(
            value = hours,
            onIncrement = { onTimeChanged((hours + 1) % HOURS_IN_DAY, minutes) },
            onDecrement = { onTimeChanged((hours - 1 + HOURS_IN_DAY) % HOURS_IN_DAY, minutes) },
        )
        Text(
            text = ":",
            style = AppTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = AppTheme.colors.textSecondary,
            modifier = Modifier.width(24.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
        )
        TimeColumn(
            value = minutes,
            onIncrement = { onTimeChanged(hours, (minutes + MINUTE_STEP) % MINUTES_IN_HOUR) },
            onDecrement = {
                onTimeChanged(hours, (minutes - MINUTE_STEP + MINUTES_IN_HOUR) % MINUTES_IN_HOUR)
            },
        )
    }
}

@Composable
private fun TimeColumn(
    value: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(onClick = onIncrement) {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowUp,
                contentDescription = null,
                tint = AppTheme.colors.textSecondary,
            )
        }
        Box(
            modifier = Modifier
                .size(width = 72.dp, height = 64.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(AppTheme.colors.surfaceVariant)
                .border(
                    width = 1.5.dp,
                    color = AppTheme.colors.border,
                    shape = RoundedCornerShape(14.dp),
                ),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = String.format(Locale.getDefault(), "%02d", value),
                style = AppTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = AppTheme.colors.textPrimary,
            )
        }
        IconButton(onClick = onDecrement) {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowDown,
                contentDescription = null,
                tint = AppTheme.colors.textSecondary,
            )
        }
    }
}
