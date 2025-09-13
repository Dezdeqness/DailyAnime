package com.dezdeqness.presentation.features.settings.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.dezdeqness.core.ui.theme.AppTheme

private const val SubTitleAlpha = 0.8f

@Composable
fun ProgressSettingsView(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    progress: Float,
    contentColor: Color = AppTheme.colors.onPrimary,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
) {
    ProgressSettingsView(
        modifier = modifier,
        title = {
            Text(
                text = title,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
                style = AppTheme.typography.bodyLarge,
                color = AppTheme.colors.textPrimary,
            )
        },
        subTitle = {
            Text(
                text = subtitle,
                modifier = Modifier.alpha(SubTitleAlpha),
                style = AppTheme.typography.bodySmall,
                color = AppTheme.colors.textPrimary,
            )
        },
        progress = {
            LinearProgressIndicator(
                modifier = Modifier.padding(vertical = 8.dp).fillMaxWidth(1f).height(4.dp),
                progress = { progress },
                drawStopIndicator = {},
            )
        },
        contentColor = contentColor,
        contentPadding = contentPadding,
    )
}

@Composable
fun ProgressSettingsView(
    modifier: Modifier = Modifier,
    title: @Composable ColumnScope.() -> Unit,
    subTitle: @Composable ColumnScope.() -> Unit,
    progress: @Composable ColumnScope.() -> Unit,
    contentColor: Color = AppTheme.colors.onPrimary,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
) {
    Box(
        modifier = modifier
            .background(contentColor)
            .padding(contentPadding)
    ) {
        Column {
            title.invoke(this)
            progress.invoke(this)
            subTitle.invoke(this)
        }
    }
}
