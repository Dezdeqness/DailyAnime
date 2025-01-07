package com.dezdeqness.presentation.features.calendar.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dezdeqness.core.ui.shimmer

@Composable
fun ShimmerCalendarLoading(
    modifier: Modifier = Modifier,
    times: Int = 2,
) {
    Column(
        modifier = modifier
            .verticalScroll(
                state = rememberScrollState(),
                enabled = false,
            )
    ) {
        repeat(times) { _ ->
            ShimmerCalendarHeader(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp))
            ShimmerCalendarItem(
                Modifier.padding(
                    vertical = 4.dp,
                    horizontal = 16.dp,
                )
            )
            ShimmerCalendarItem(
                Modifier.padding(
                    vertical = 4.dp,
                    horizontal = 16.dp,
                )
            )
        }
    }
}

@Composable
private fun ShimmerCalendarHeader(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .width(140.dp)
            .height(18.dp)
            .shimmer()
    )
}

@Composable
private fun ShimmerCalendarItem(
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
                    .height(18.dp)
                    .shimmer()
            )

            Box(
                modifier = Modifier
                    .weight(2f)
                    .height(150.dp)
                    .shimmer()
            )

            Column(
                modifier = Modifier
                    .weight(3f)
                    .padding(start = 8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(18.dp)
                        .shimmer()
                )

                Spacer(modifier = Modifier.size(4.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(18.dp)
                        .shimmer()
                )

                Spacer(modifier = Modifier.size(4.dp))

                Box(
                    modifier = Modifier
                        .width(80.dp)
                        .height(14.dp)
                        .shimmer()
                )

                Spacer(modifier = Modifier.size(4.dp))

                Box(
                    modifier = Modifier
                        .width(30.dp)
                        .height(14.dp)
                        .shimmer()
                )
            }
        }
    }
}

