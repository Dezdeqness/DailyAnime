package com.dezdeqness.feature.topics.presentation.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.Surface
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.dezdeqness.feature.topics.presentation.models.TopicListUiModel
import com.dezdeqness.feature.topics.presentation.preview.TopicsPreviewData
import com.dezdeqness.foundation.ui.theme.AppTheme
import com.dezdeqness.foundation.ui.views.PaginationEffect

@Composable
fun TopicListContent(
    list: List<TopicListUiModel>,
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
                is TopicListUiModel.Item -> {
                    TopicListItem(
                        item = item,
                        onClick = { onItemClicked(item.content.topicId) },
                        modifier = Modifier.animateItem(),
                    )
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
fun TopicListContentPreview() {
    AppTheme {
        Surface(color = AppTheme.colors.background) {
            TopicListContent(
                list = TopicsPreviewData.list,
                hasNextPage = false,
                isPageLoading = false,
                onLoadMore = {},
                onItemClicked = {},
            )
        }
    }
}
