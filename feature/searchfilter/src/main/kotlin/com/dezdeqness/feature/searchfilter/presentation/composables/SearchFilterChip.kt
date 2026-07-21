package com.dezdeqness.feature.searchfilter.presentation.composables

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dezdeqness.foundation.ui.theme.AppTheme

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
                color = if (isSelected) AppTheme.colors.white else AppTheme.colors.textPrimary,
            )
        },
        colors = FilterChipDefaults.filterChipColors().copy(
            containerColor = AppTheme.colors.surfaceVariant,
            selectedContainerColor = AppTheme.colors.accent,
        ),
        shape = RoundedCornerShape(50),
        elevation = null,
        selected = isSelected,
        border = null,
    )
}
