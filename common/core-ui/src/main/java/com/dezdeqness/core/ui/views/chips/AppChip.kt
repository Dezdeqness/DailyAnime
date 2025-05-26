package com.dezdeqness.core.ui.views.chips

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.SelectableChipColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import com.dezdeqness.core.ui.theme.AppTheme

@Composable
fun AppChip(
    modifier: Modifier = Modifier,
    title: String,
    titleStyle: TextStyle = AppTheme.typography.bodyMedium,
    titleColor: Color = AppTheme.colors.textPrimary,
    shape: Shape = RoundedCornerShape(50),
    colors: SelectableChipColors =  FilterChipDefaults.filterChipColors().copy(
        containerColor = AppTheme.colors.onPrimary,
        selectedContainerColor = AppTheme.colors.onPrimary
    ),
    onClick: (() -> Unit)? = null,
) {
    FilterChip(
        modifier = modifier,
        onClick = {
            onClick?.invoke()
        },
        label = {
            Text(
                title,
                color = titleColor,
                style = titleStyle,
            )
        },
        colors = colors,
        shape = shape,
        elevation = null,
        selected = false,
    )
}
