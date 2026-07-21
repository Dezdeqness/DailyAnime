package com.dezdeqness.feature.searchfilter.presentation.composables

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
import com.dezdeqness.contract.filter.model.SearchSectionUiModel
import com.dezdeqness.feature.searchfilter.R
import com.dezdeqness.foundation.ui.ExpandableContent
import com.dezdeqness.foundation.ui.views.buttons.AppTextButton

private const val ITEMS_EXPAND_THRESHOLD = 6
private const val ITEMS_VISIBLE_COUNT = 8

@Composable
fun SearchFilterCheckboxGroup(
    modifier: Modifier = Modifier,
    section: SearchSectionUiModel,
    onScrollNeed: () -> Unit,
    onClick: (String, String, Boolean) -> Unit,
) {
    val items = section.items
    val selectedItems = section.selectedCells

    val isItemsExceedsLimit = items.size > ITEMS_EXPAND_THRESHOLD

    var isCollapsed by remember {
        mutableStateOf(true)
    }

    val mainItems = remember {
        items.take(ITEMS_VISIBLE_COUNT)
    }

    val allItems = remember {
        items.drop(ITEMS_VISIBLE_COUNT)
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
                onClick = { id, selected ->
                    onClick.invoke(section.innerId, id, selected)
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
                        onClick = { id, selected ->
                            onClick.invoke(section.innerId, id, selected)
                        },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }

        if (isItemsExceedsLimit) {
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
                },
            )
        }
    }
}
