package com.dezdeqness.feature.personallist.composable

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dezdeqness.core.ui.views.PaginationEffect
import com.dezdeqness.feature.personallist.model.UserRateUiModel
import com.dezdeqness.feature.personallist.tab.PersonalListAction

@Composable
fun PersonalList(
    modifier: Modifier = Modifier,
    list: List<UserRateUiModel>,
    hasNextPage: Boolean,
    isPageLoading: Boolean,
    onActionReceive: (PersonalListAction) -> Unit,
    onLoadMore: () -> Unit,
) {
    val listState = rememberLazyListState()

    PaginationEffect(
        listState = listState,
        hasNextPage = hasNextPage,
        isPageLoading = isPageLoading,
        onLoadMore = onLoadMore,
    )

    LazyColumn(
        state = listState,
        modifier = modifier,
    ) {
        items(
            count = list.size,
            key = { index -> list[index].id + list[index].name.hashCode() },
        ) { index ->
            PersonalListAnimeItem(
                userRateUiModel = list[index],
                modifier = Modifier
                    .animateItem()
                    .padding(
                        vertical = 4.dp,
                        horizontal = 16.dp,
                    ),
                onActionReceive = onActionReceive,
            )
        }

        if (hasNextPage) {
            item {
                ShimmerPersonalItem(
                    modifier = Modifier.padding(
                        vertical = 4.dp,
                        horizontal = 16.dp,
                    )
                )
            }
        }
    }
}
