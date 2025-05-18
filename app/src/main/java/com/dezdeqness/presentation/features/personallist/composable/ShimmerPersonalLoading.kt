package com.dezdeqness.presentation.features.personallist.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.dezdeqness.core.ui.views.shimmer

@Composable
fun ShimmerPersonalLoading(
    modifier: Modifier = Modifier,
    times: Int = 7,
) {
    Column(
        modifier = modifier
            .verticalScroll(
                state = rememberScrollState(),
                enabled = false,
            )
    ) {
        repeat(times) { _ ->
            ShimmerPersonalItem()
            Spacer(modifier = Modifier.size(8.dp))
        }
    }
}

@Composable
private fun ShimmerPersonalItem(
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        Row(
            modifier = Modifier.height(IntrinsicSize.Max)
        ) {

            Box(
                modifier = Modifier
                    .height(140.dp)
                    .aspectRatio(2f / 3)
                    .shimmer()
            )

            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .width(100.dp)
                            .height(16.dp)
                            .shimmer()
                    )

                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .width(30.dp)
                            .height(14.dp)
                            .shimmer()
                    )
                }

                Column {
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(8.dp)
                                .width(30.dp)
                                .height(16.dp)
                                .shimmer()
                        )

                        Row(horizontalArrangement = Arrangement.Absolute.SpaceEvenly) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .padding(12.dp)
                                    .clip(CircleShape)
                                    .shimmer()
                            )

                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .padding(12.dp)
                                    .clip(CircleShape)
                                    .shimmer()
                            )
                        }
                    }
                }
            }

        }
    }
}
