package com.dezdeqness.presentation.features.animelist.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dezdeqness.core.ui.shimmer

@Composable
fun ShimmerSearchLoading(
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
            ShimmerSearchPair()
        }
    }
}

@Composable
fun ShimmerSearchItemLoading(
    modifier: Modifier = Modifier,
) {
    ShimmerSearchItem(modifier)
}


@Composable
private fun ShimmerSearchPair(
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        ShimmerSearchItem(modifier = Modifier.weight(1f))

        Spacer(modifier = Modifier.size(8.dp))

        ShimmerSearchItem(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun ShimmerSearchItem(
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .shimmer()
            )

            Column(
                modifier = Modifier
                    .padding(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(14.dp)
                        .shimmer()
                )

                Spacer(modifier = Modifier.size(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(14.dp)
                        .shimmer()
                )
            }
        }
    }
}
