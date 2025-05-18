package com.dezdeqness.presentation.features.animedetails.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.dezdeqness.core.ui.views.shimmer

@Composable
fun ShimmerDetailsLoading(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 56.dp)
            .verticalScroll(
                state = rememberScrollState(),
                enabled = false,
            ),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .size(width = 200.dp, height = 264.dp)
                .clip(RoundedCornerShape(15.dp))
                .shimmer()
                .align(Alignment.CenterHorizontally)
        )

        Box(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .width(80.dp)
                .height(20.dp)
                .shimmer()
                .align(Alignment.CenterHorizontally)
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            userScrollEnabled = false,
        ) {
            items(5) {
                Box(
                    modifier = Modifier
                        .width(60.dp)
                        .height(20.dp)
                        .shimmer()
                )
            }
        }

        Box(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .width(80.dp)
                .height(20.dp)
                .shimmer()
        )

        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            repeat(4) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                        .shimmer()
                )
            }
        }

        Box(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .width(80.dp)
                .height(20.dp)
                .shimmer()
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            userScrollEnabled = false,
        ) {
            items(5) {
                Box(
                    modifier = Modifier
                        .width(120.dp)
                        .height(170.dp)
                        .shimmer()
                )
            }
        }
    }
}
