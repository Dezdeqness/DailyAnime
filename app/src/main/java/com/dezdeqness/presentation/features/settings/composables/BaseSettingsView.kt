package com.dezdeqness.presentation.features.settings.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.presentation.action.Action

internal val MinHeight = 56.dp

@Composable
internal fun BaseSettingsView(
    modifier: Modifier = Modifier,
    title: String,
    subTitle: @Composable (ColumnScope.() -> Unit)? = null,
    icon: @Composable (() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    widget: @Composable (() -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .sizeIn(minHeight = MinHeight)
            .clickable(
                enabled = onClick != null,
                onClick = {
                    onClick?.invoke()
                },
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = AppTheme.colors.ripple),
            )
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (icon != null) {
            Box(
                modifier = Modifier.padding(end = 8.dp),
                content = { icon() },
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 16.dp),
        ) {
                Text(
                    text = title,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                    style = AppTheme.typography.titleLarge,
                    color = AppTheme.colors.textPrimary,
                )
            subTitle?.invoke(this)
        }
        if (widget != null) {
            Box(
                modifier = Modifier.padding(start = 8.dp),
                content = { widget() },
            )
        }
    }
}
