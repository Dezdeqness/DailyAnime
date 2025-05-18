package com.dezdeqness.presentation.features.animedetails.composables.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.presentation.models.AnimeCellList

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DetailsGenres(
    modifier: Modifier = Modifier,
    genreCells: AnimeCellList,
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(genreCells.list.size) {
            val item = genreCells.list[it]

            FilterChip(
                modifier = modifier,
                onClick = {},
                label = {
                    Text(
                        text = item.displayName,
                        color = AppTheme.colors.textPrimary,
                        style = AppTheme.typography.bodyMedium,
                    )
                },
                colors = FilterChipDefaults.filterChipColors().copy(
                    containerColor = AppTheme.colors.onPrimary,
                    selectedContainerColor = AppTheme.colors.onPrimary,
                ),
                shape = RoundedCornerShape(50),
                elevation = null,
                selected = false,
            )
        }
    }
}
