package com.dezdeqness.feature.onboarding.flow.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dezdeqness.feature.onboarding.R
import com.dezdeqness.foundation.ui.theme.AppTheme

@Composable
fun DoneStep(
    selectedGenreNames: List<String>,
    notificationsEnabled: Boolean,
    notificationTimeLabel: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(AppTheme.colors.primary),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(48.dp),
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = stringResource(R.string.onboarding_done_title),
            style = AppTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold),
            color = AppTheme.colors.textPrimary,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.onboarding_done_subtitle),
            style = AppTheme.typography.bodyMedium,
            color = AppTheme.colors.textSecondary,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(AppTheme.colors.surface)
                .border(
                    width = 1.dp,
                    color = AppTheme.colors.border,
                    shape = RoundedCornerShape(16.dp),
                )
                .padding(20.dp),
        ) {
            Text(
                text = stringResource(R.string.onboarding_done_summary_label),
                style = AppTheme.typography.bodySmall,
                color = AppTheme.colors.textSecondary,
            )
            Spacer(modifier = Modifier.height(12.dp))
            SummaryRow(
                label = stringResource(R.string.onboarding_done_summary_genres),
                value = selectedGenreNames.takeIf { it.isNotEmpty() }?.joinToString(", ")
                    ?: stringResource(R.string.onboarding_done_summary_none),
            )
            Spacer(modifier = Modifier.height(10.dp))
            SummaryRow(
                label = stringResource(R.string.onboarding_done_summary_notifications),
                value = if (notificationsEnabled) {
                    stringResource(R.string.onboarding_done_summary_notifications_on, notificationTimeLabel)
                } else {
                    stringResource(R.string.onboarding_done_summary_notifications_off)
                },
                valueColor = if (notificationsEnabled) {
                    AppTheme.colors.primary
                } else {
                    AppTheme.colors.textPrimary
                },
            )
        }
    }
}

@Composable
private fun SummaryRow(
    label: String,
    value: String,
    valueColor: Color = AppTheme.colors.textPrimary,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = label,
            style = AppTheme.typography.bodyMedium,
            color = AppTheme.colors.textSecondary,
        )
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = value,
            style = AppTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            color = valueColor,
            textAlign = TextAlign.End,
        )
    }
}
