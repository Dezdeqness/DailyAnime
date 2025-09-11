package com.dezdeqness.presentation.features.settings.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.dezdeqness.core.ui.theme.AppTheme

internal val MinHeight = 56.dp

@Composable
internal fun BaseSettingsView(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    title: @Composable ColumnScope.() -> Unit,
    subTitle: @Composable (ColumnScope.() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    contentColor: Color = AppTheme.colors.onPrimary,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
    prefixIcon: (@Composable (Modifier) -> Unit)? = null,
    suffixIcon: (@Composable (Modifier) -> Unit)? = null,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .sizeIn(minHeight = MinHeight)
            .background(contentColor)
            .clickable(
                enabled = onClick != null,
                onClick = {
                    onClick?.invoke()
                },
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = AppTheme.colors.ripple),
            )
            .padding(contentPadding)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            val iconModifier = Modifier.size(24.dp)

            prefixIcon?.invoke(iconModifier)
            Column(modifier = Modifier.weight(1f)) {
                title.invoke(this)
                subTitle?.invoke(this)
            }
            suffixIcon?.invoke(iconModifier)
        }

        if (enabled.not()) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(contentColor.copy(alpha = 0.7f))
            )
        }
    }
}
