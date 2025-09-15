package com.dezdeqness.core.ui.views.header

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dezdeqness.core.ui.theme.AppTheme

@Composable
fun Header(
    modifier: Modifier = Modifier,
    title: String,
    titleStyle: TextStyle = AppTheme.typography.labelLarge,
    titleColor: Color = AppTheme.colors.textPrimary,
    verticalPadding: Dp = 12.dp,
) {
    Header(
        modifier = modifier,
        title = title,
        titleStyle = titleStyle,
        titleColor = titleColor,
        verticalPadding = verticalPadding,
        onClick = null,
    )
}

@Composable
fun Header(
    modifier: Modifier = Modifier,
    title: String,
    titleStyle: TextStyle = AppTheme.typography.labelLarge,
    titleColor: Color = AppTheme.colors.textPrimary,
    icon: ImageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
    iconColor: Color = AppTheme.colors.onSurface,
    verticalPadding: Dp = 12.dp,
    onClick: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .clickable(
                enabled = onClick != null,
                onClick = onClick ?: {},
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = AppTheme.colors.ripple),
            )
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            title,
            style = titleStyle,
            modifier = Modifier.padding(vertical = verticalPadding),
            color = titleColor
        )

        if (onClick != null) {
            Icon(
                icon,
                contentDescription = null,
                tint = iconColor,
            )
        }
    }
}

@PreviewLightDark
@Composable
fun HeaderPreview() {
    AppTheme {
        Header(
            modifier = Modifier
                .background(AppTheme.colors.onPrimary)
                .fillMaxWidth(),
            title = "Sci-Fi",
            onClick = {},
        )
    }
}
