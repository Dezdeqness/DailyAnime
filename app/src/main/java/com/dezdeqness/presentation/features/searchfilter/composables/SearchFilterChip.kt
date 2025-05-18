package com.dezdeqness.presentation.features.searchfilter.composables

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.dezdeqness.core.ui.theme.AppTheme

@Composable
fun SearchFilterChip(
    displayName: String,
    id: String,
    isSelected: Boolean,
    onClick: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    FilterChip(
        modifier = modifier,
        onClick = {
            onClick(id, isSelected)
        },
        label = {
            Text(
                displayName,
                color = if (isSelected) Color.White else AppTheme.colors.textPrimary,
            )
        },
        colors = FilterChipDefaults.filterChipColors().copy(
            containerColor = AppTheme.colors.onPrimary,
            selectedContainerColor = AppTheme.colors.accent,
        ),
        shape = RoundedCornerShape(50),
        elevation = FilterChipDefaults.filterChipElevation(elevation = 2.dp),
        selected = isSelected,
        border = null,
    )

}
