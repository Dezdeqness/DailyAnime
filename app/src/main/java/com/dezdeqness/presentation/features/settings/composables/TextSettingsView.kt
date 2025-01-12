package com.dezdeqness.presentation.features.settings.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.dezdeqness.core.ui.theme.AppTheme

private const val SubTitleAlpha = 0.8f

@Composable
fun TextSettingsView(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String? = null,
    icon: ImageVector? = null,
    iconTint: Color = AppTheme.colors.textPrimary,
    widget: @Composable (() -> Unit)? = null,
    onSettingClick: (() -> Unit)? = null,
) {
    BaseSettingsView(
        modifier = modifier,
        title = title,
        subTitle = if (!subtitle.isNullOrBlank()) {
            {
                Text(
                    text = subtitle,
                    modifier = Modifier.alpha(SubTitleAlpha),
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTheme.colors.textPrimary,
                )
            }
        } else {
            null
        },
        icon = if (icon != null) {
            {
                Icon(
                    imageVector = icon,
                    tint = iconTint,
                    contentDescription = null,
                )
            }
        } else {
            null
        },
        onClick = onSettingClick,
        widget = widget,
    )
}

@PreviewLightDark
@Composable
fun TextSettingPreview() {
    AppTheme {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AppTheme.colors.onPrimary)
                ) {
                TextSettingsView(
                    title = "Title1",
                )
                TextSettingsView(
                    title = "Title1",
                    subtitle = "Subtitle1",
                )
                TextSettingsView(
                    title = "Title1",
                    subtitle = "Subtitle1",
                    icon = Icons.Outlined.Settings,
                )
            }
        }
}
