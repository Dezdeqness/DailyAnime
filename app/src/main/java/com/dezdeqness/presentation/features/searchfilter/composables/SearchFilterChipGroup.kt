package com.dezdeqness.presentation.features.searchfilter.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dezdeqness.presentation.models.SearchSectionUiModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SearchFilterChipGroup(
    modifier: Modifier = Modifier,
    section: SearchSectionUiModel,
    onClick: (String,String, Boolean) -> Unit,
) {
    val items = section.items
    val selectedItems = section.selectedCells

    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items.forEach { item ->
            SearchFilterChip(
                displayName = item.displayName,
                id = item.id,
                isSelected = selectedItems.contains(item.id),
                onClick = { id, isSelected ->
                    onClick.invoke(section.innerId, id, isSelected)
                }
            )
        }
    }
}
