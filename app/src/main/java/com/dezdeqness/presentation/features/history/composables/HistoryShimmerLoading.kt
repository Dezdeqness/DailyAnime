package com.dezdeqness.presentation.features.history.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dezdeqness.core.ui.shimmer


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
            )
    ) {
        repeat(times) { _ ->
            ShimmerSearchItem()
        }
    }
}

@Composable
fun ShimmerSearchItem(
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.padding(bottom = 8.dp)) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            Box(
                modifier = Modifier
                    .width(140.dp)
                    .height(18.dp)
                    .shimmer()
            )

            Column(verticalArrangement = Arrangement.spacedBy(4.dp),) {
                Box(
                    modifier = Modifier
                        .width(100.dp)
                        .height(14.dp)
                        .shimmer()
                )

                Box(
                    modifier = Modifier
                        .width(60.dp)
                        .height(14.dp)
                        .shimmer()
                )
            }
        }
    }
}
