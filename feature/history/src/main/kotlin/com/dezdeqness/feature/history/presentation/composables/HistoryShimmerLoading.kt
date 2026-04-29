package com.dezdeqness.feature.history.presentation.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dezdeqness.foundation.ui.views.shimmer

@Composable
fun HistoryShimmerLoading(
    modifier: Modifier = Modifier,
    times: Int = 3,
) {
    Column(
        modifier = modifier
            .verticalScroll(
                state = rememberScrollState(),
                enabled = false,
            ),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(times) { _ ->
            HistoryShimmerItem()
        }
    }
}

@Composable
fun HistoryShimmerItem(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
            .shimmer()
    )
}
