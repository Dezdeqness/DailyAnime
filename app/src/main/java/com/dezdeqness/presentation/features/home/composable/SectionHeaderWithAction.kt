package com.dezdeqness.presentation.features.home.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.presentation.action.Action

@Composable
fun SectionHeaderWithAction(
    modifier: Modifier = Modifier,
    title: String,
    onActionReceive: (Action) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                onClick = { onActionReceive.invoke(Action.CalendarHeaderClicked) },
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = AppTheme.colors.ripple),
            )
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            title,
            style = AppTheme.typography.labelLarge.copy(fontSize = 20.sp),
            modifier = Modifier.padding(vertical = 16.dp),
            color = AppTheme.colors.textPrimary
        )

        Icon(
            Icons.AutoMirrored.Default.KeyboardArrowRight,
            contentDescription = null,
            tint = AppTheme.colors.onSurface
        )
    }
}

@PreviewLightDark
@Composable
fun SectionHeaderWithActionPreview() {
    AppTheme {
        Box(
            modifier = Modifier
                .background(AppTheme.colors.onPrimary)
                .padding(16.dp)
                .width(200.dp)
        ) {
            SectionHeaderWithAction(
                title = "Sci-Fi",
                onActionReceive = {},
            )
        }
    }
}
