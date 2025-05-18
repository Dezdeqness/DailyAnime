package com.dezdeqness.presentation.features.home.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dezdeqness.core.ui.views.shimmer

@Composable
fun ShimmerHomeLoading(
    modifier: Modifier = Modifier,
    times: Int = 4,
) {
    Column(
        modifier = modifier
    ) {
        repeat(times) { _ ->
            ShimmerHomeHeader(
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            ShimmerHomeItem()
        }
    }
}

@Composable
private fun ShimmerHomeHeader(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .padding(vertical = 16.dp)
            .width(100.dp)
            .height(20.dp)
            .shimmer()
    )
}

@Composable
private fun ShimmerHomeItem(
    modifier: Modifier = Modifier,
    times: Int = 5,
) {
    Box(modifier = modifier) {
        LazyRow (
            contentPadding = PaddingValues(horizontal = 16.dp),
        ) {
            items(count = times) {
                Column(
                    modifier = Modifier.padding(end = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Box(
                        modifier = Modifier
                            .width(100.dp)
                            .height(120.dp)
                            .shimmer()
                    )
                    Spacer(modifier = Modifier.size(8.dp))

                    Box(
                        modifier = Modifier
                            .width(80.dp)
                            .height(14.dp)
                            .shimmer()
                    )

                    Spacer(modifier = Modifier.size(4.dp))

                    Box(
                        modifier = Modifier
                            .width(80.dp)
                            .height(14.dp)
                            .shimmer()
                    )
                }
            }
        }
    }
}
