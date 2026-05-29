package com.dezdeqness.feature.history.presentation.composables

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.Surface
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.dezdeqness.foundation.ui.theme.AppTheme
import com.dezdeqness.foundation.ui.views.PaginationEffect
import com.dezdeqness.feature.history.presentation.models.HistoryModel
import com.dezdeqness.feature.history.presentation.preview.HistoryPreviewData

@Composable
fun HistoryList(
    modifier: Modifier = Modifier,
    list: List<HistoryModel>,
    hasNextPage: Boolean,
    isPageLoading: Boolean,
    onLoadMore: () -> Unit,
) {
    val state = rememberLazyListState()

    PaginationEffect(
        listState = state,
        hasNextPage = hasNextPage,
        isPageLoading = isPageLoading,
        onLoadMore = onLoadMore,
    )

    LazyColumn(
        state = state,
        modifier = modifier.fillMaxSize(),
    ) {
        items(
            count = list.size,
            key = { index ->
                val item = list[index]
                item.id() + item.hashCode()
            }
        ) { index ->
            when (val item = list[index]) {
                is HistoryModel.HistoryUiModel -> {
                    HistoryItem(
                        item = item,
                        modifier = Modifier
                            .animateItem()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
                is HistoryModel.HistoryHeaderUiModel -> {
                    HistoryHeader(
                        header = item.header,
                        modifier = Modifier
                            .animateItem()
                            .padding(vertical = 8.dp, horizontal = 16.dp)
                    )
                }
            }
        }

        if (hasNextPage) {
            item {
                HistoryShimmerItem(modifier = Modifier.padding(horizontal = 16.dp))
            }
        }

    }
}

@PreviewLightDark
@Composable
fun HistoryListPreview() {
    AppTheme {
        Surface(color = AppTheme.colors.background) {
            HistoryList(
                list = HistoryPreviewData.list,
                hasNextPage = false,
                isPageLoading = false,
                onLoadMore = {},
            )
        }
    }
}
