package com.dezdeqness.feature.news.presentation.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dezdeqness.feature.news.presentation.models.NewsUiModel
import com.dezdeqness.foundation.ui.views.PaginationEffect

@Composable
fun NewsList(
    list: List<NewsUiModel>,
    hasNextPage: Boolean,
    isPageLoading: Boolean,
    onLoadMore: () -> Unit,
    onItemClicked: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()

    PaginationEffect(
        listState = listState,
        isPageLoading = isPageLoading,
        onLoadMore = onLoadMore,
        hasNextPage = hasNextPage,
    )

    LazyColumn(
        state = listState,
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(
            items = list,
            key = { it.id() + it.hashCode() },
        ) { item ->
            when (item) {
                is NewsUiModel.NewsItem -> {
                    NewsItem(
                        item = item,
                        onClick = { onItemClicked(item.content.topicId) },
                        modifier = Modifier.animateItem(),
                    )
                }
            }
        }
    }
}
