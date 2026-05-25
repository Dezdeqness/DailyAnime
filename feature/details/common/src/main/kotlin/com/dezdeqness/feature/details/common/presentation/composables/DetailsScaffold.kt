package com.dezdeqness.feature.details.common.presentation.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.dezdeqness.feature.details.common.presentation.store.DetailsStatus
import com.dezdeqness.foundation.ui.theme.AppTheme
import com.dezdeqness.foundation.ui.views.RetryError

@Composable
fun DetailsScaffold(
    status: DetailsStatus,
    onRetryClick: () -> Unit,
    loading: @Composable () -> Unit,
    content: @Composable (LazyListState) -> Unit,
    toolbar: @Composable (toolbarColor: Color) -> Unit,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
    overlay: @Composable BoxScope.() -> Unit = {},
) {
    val isScrolledPast by remember {
        derivedStateOf { listState.firstVisibleItemIndex >= 1 }
    }
    val toolbarBackgroundColor =
        if (isScrolledPast) AppTheme.colors.onPrimary else Color.Transparent

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.TopStart) {
        when (status) {
            DetailsStatus.Initial, DetailsStatus.Loading -> {
                loading()
            }

            DetailsStatus.Loaded -> {
                content(listState)
            }

            DetailsStatus.Error -> {
                RetryError(onRetryClick = onRetryClick)
            }
        }

        toolbar(toolbarBackgroundColor)

        overlay()
    }
}
