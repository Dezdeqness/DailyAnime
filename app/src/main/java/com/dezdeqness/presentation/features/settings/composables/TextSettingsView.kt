package com.dezdeqness.presentation.features.settings.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.dezdeqness.R
import com.dezdeqness.core.ui.theme.AppTheme

private const val SubTitleAlpha = 0.8f

@Composable
fun TextSettingsView(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String? = null,
    enabled: Boolean = true,
    icon: ImageVector? = null,
    iconTint: Color = AppTheme.colors.textPrimary,
    widget: @Composable (() -> Unit)? = null,
    onSettingClick: (() -> Unit)? = null,
) {
    Box {
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
            onClick = if (enabled) onSettingClick else null,
            widget = widget,
        )

        if (enabled.not()) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(colorResource(id = R.color.background_tint).copy(alpha = 0.8f))
            )
        }
    }
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
            TextSettingsView(
                title = "Title1",
                subtitle = "Subtitle1",
                icon = Icons.Outlined.Settings,
                enabled = false
            )
        }
    }
}
