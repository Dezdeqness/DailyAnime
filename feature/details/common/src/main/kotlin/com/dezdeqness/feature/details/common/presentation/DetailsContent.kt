package com.dezdeqness.feature.details.common.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dezdeqness.feature.details.common.presentation.utils.LocalDetailsRenderManagerComposition
import com.dezdeqness.foundation.ui.theme.AppTheme

@Composable
fun <UiEvent : Any> DetailsContent(
    sections: List<DetailsSection>,
    onEvent: (UiEvent) -> Unit,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
) {
    val manager = LocalDetailsRenderManagerComposition.current

    LazyColumn(
        state = listState,
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.colors.onPrimary),
    ) {
        items(
            count = sections.size,
            key = { index -> sections[index].key(index) },
        ) { index ->
            val section = sections[index]
            manager.OnRenderByType(section, onEvent)
        }
    }
}
