package com.dezdeqness.presentation.features.searchfilter.composables

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.dezdeqness.R
import com.dezdeqness.core.ui.ExpandableContent
import com.dezdeqness.core.ui.views.AppTextButton
import com.dezdeqness.presentation.models.SearchSectionUiModel

@Composable
fun SearchFilterCheckboxGroup(
    modifier: Modifier = Modifier,
    section: SearchSectionUiModel,
    onScrollNeed: () -> Unit,
    onClick: (String, String, Boolean) -> Unit,
) {
    val items = section.items
    val selectedItems = section.selectedCells

    var isCollapsed by remember {
        mutableStateOf(true)
    }

    val mainItems = remember {
        items.take(8)
    }

    val allItems = remember {
        items.drop(8)
    }

    Column(
        modifier = modifier.animateContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        mainItems.forEach { item ->
            val isSelected = selectedItems.contains(item.id)
            SearchFilterCheckbox(
                displayName = item.displayName,
                id = item.id,
                isSelected = isSelected,
                onClick = { id, isSelected ->
                    onClick.invoke(section.innerId, id, isSelected)
                },
                modifier = Modifier.fillMaxWidth(),
            )
        }
        ExpandableContent(isVisible = isCollapsed.not()) {
            Column {
                allItems.forEach { item ->
                    val isSelected = selectedItems.contains(item.id)
                    SearchFilterCheckbox(
                        displayName = item.displayName,
                        id = item.id,
                        isSelected = isSelected,
                        onClick = { id, isSelected ->
                            onClick.invoke(section.innerId, id, isSelected)
                        },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }

        AppTextButton(
            title = if (isCollapsed) {
                stringResource(R.string.search_filter_show_more_title)
            } else {
                stringResource(R.string.search_filter_collapse_title)
            },
            onClick = {
                if (!isCollapsed) {
                    onScrollNeed()
                }

                isCollapsed = !isCollapsed
            }
        )
    }
}
