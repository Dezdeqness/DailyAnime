package com.dezdeqness.feature.search.presentation.history

import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.dezdeqness.feature.search.presentation.composables.HistoryItem
import com.dezdeqness.foundation.ui.theme.AppTheme

@Composable
fun SearchHistory(
    history: List<String>,
    onItemClick: (String) -> Unit,
    onRemoveClick: (String) -> Unit,
    onFillClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier) {
        items(
            count = history.size,
            key = { index -> history[index] },
        ) { index ->
            val item = history[index]
            HistoryItem(
                modifier = Modifier.animateItem(),
                title = item,
                onClicked = { onItemClick(item) },
                onRemoveClicked = { onRemoveClick(item) },
                onFulFillClicked = { onFillClick(item) },
            )
        }
    }
}

@PreviewLightDark
@Composable
fun SearchHistoryPreview() {
    AppTheme {
        SearchHistory(
            modifier = Modifier.background(AppTheme.colors.onPrimary),
            history = listOf("Naruto", "One Piece", "Bleach"),
            onItemClick = {},
            onRemoveClick = {},
            onFillClick = {},
        )
    }
}
