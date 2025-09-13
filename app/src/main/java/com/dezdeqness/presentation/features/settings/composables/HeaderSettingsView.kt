package com.dezdeqness.presentation.features.settings.composables

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.dezdeqness.core.ui.theme.AppTheme

@Composable
fun HeaderSettingsView(
    modifier: Modifier = Modifier,
    title: String
) {
    TextSettingsView(
        modifier = modifier,
        title = title,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Composable
fun HeaderCustomSettingsView(
    modifier: Modifier = Modifier,
    title: String,
    titleStyle: TextStyle = AppTheme.typography.titleMedium,
    titleColor: Color = AppTheme.colors.primary,
) {
    TextSettingsView(
        modifier = modifier,
        title = {
            Text(
                text = title,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
                style = titleStyle,
                color = titleColor,
            )
        },
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    )
}
