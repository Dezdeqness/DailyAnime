package com.dezdeqness.presentation.features.settings.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.dezdeqness.core.ui.theme.AppTheme

@Composable
fun SwitchSettingsView(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String? = null,
    enabled: Boolean = true,
    prefixIcon: (@Composable (Modifier) -> Unit)? = null,
    checked: Boolean = false,
    onCheckedChanged: (Boolean) -> Unit,
) {
    TextSettingsView(
        modifier = modifier,
        title = title,
        subtitle = subtitle,
        onClick = { onCheckedChanged(!checked) },
        enabled = enabled,
        prefixIcon = prefixIcon,
        suffixIcon = {
            Switch(
                checked = checked,
                onCheckedChange = null,
            )
        },
    )
}

@PreviewLightDark
@Composable
fun SwitchSettingsViewPreview() {
    AppTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(AppTheme.colors.onPrimary)
        ) {
            SwitchSettingsView(
                title = "Title1",
                onCheckedChanged = {},
            )
            SwitchSettingsView(
                title = "Title1",
                checked = true,
                onCheckedChanged = {},
            )
            SwitchSettingsView(
                title = "Title1",
                subtitle = "Subtitle1",
                onCheckedChanged = {},
            )
            SwitchSettingsView(
                title = "Title1",
                subtitle = "Subtitle1",
                prefixIcon = { modifier ->
                    Icon(
                        Icons.Outlined.Settings,
                        modifier = modifier,
                        contentDescription = null,
                        tint = AppTheme.colors.textPrimary,
                    )
                },
                onCheckedChanged = {},
            )
        }
    }
}
