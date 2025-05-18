package com.dezdeqness.presentation.features.userrate.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dezdeqness.core.ui.theme.AppTheme

@Composable
fun StatusRibbon(
    modifier: Modifier = Modifier,
    title: String,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .border(BorderStroke(1.dp, AppTheme.colors.onSurface), RoundedCornerShape(16.dp))
            .clickable(
                onClick = onClick,
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = AppTheme.colors.ripple)
            )
            .background(AppTheme.colors.onPrimary.copy(alpha = 0f))
            .padding(vertical = 12.dp, horizontal = 8.dp),
    ) {
        Text(
            text = title,
            style = AppTheme.typography.labelSmall,
            color = AppTheme.colors.textPrimary,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun StatusRibbonPreview() {
    AppTheme {
        StatusRibbon(
            title = "Смотрю",
            onClick = {},
            modifier = Modifier.width(80.dp)
        )
    }
}
