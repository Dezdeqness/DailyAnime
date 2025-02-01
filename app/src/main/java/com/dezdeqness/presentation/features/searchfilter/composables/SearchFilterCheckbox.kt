package com.dezdeqness.presentation.features.searchfilter.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.dezdeqness.core.ui.theme.AppTheme

@Composable
fun SearchFilterCheckbox(
    displayName: String,
    id: String,
    isSelected: Boolean,
    onClick: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            displayName,
            color = AppTheme.colors.textPrimary,
        )
        Checkbox(
            checked = isSelected,
            onCheckedChange = {
                onClick(id, isSelected)
            }
        )
    }
}
