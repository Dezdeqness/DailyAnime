package com.dezdeqness.presentation.features.details.composables.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dezdeqness.core.ui.views.chips.AppChip
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

            AppChip(title = item.displayName)
        }
    }
}
