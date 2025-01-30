package com.dezdeqness.presentation.features.searchfilter.composables

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.dezdeqness.R
import com.dezdeqness.core.ui.theme.AppTheme

@Composable
fun SearchFilterChip(
    displayName: String,
    id: String,
    isSelected: Boolean,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    MaterialTheme {  }
    FilterChip(
        modifier = modifier,
        onClick = {
            onClick(id)
        },
        label = {
            Text(
                displayName,
                color = if (isSelected) Color.White else AppTheme.colors.textPrimary,
            )
        },
        colors = FilterChipDefaults.filterChipColors().copy(
            containerColor = colorResource(R.color.background_tint),
            selectedContainerColor = colorResource(R.color.purple_500)
        ),
        shape = RoundedCornerShape(50),
        elevation = FilterChipDefaults.filterChipElevation(elevation = 2.dp),
        selected = isSelected,
        border = null,
    )

}
