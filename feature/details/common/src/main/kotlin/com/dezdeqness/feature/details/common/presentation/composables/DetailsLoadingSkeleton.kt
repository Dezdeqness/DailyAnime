package com.dezdeqness.feature.details.common.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dezdeqness.foundation.ui.theme.AppTheme
import com.dezdeqness.foundation.ui.views.shimmer

@Composable
fun DetailsLoadingSkeleton(
    modifier: Modifier = Modifier,
    descriptionLines: Int = 4,
    extras: @Composable ColumnScope.() -> Unit = {},
) {
    Column(
        modifier = modifier
            .background(AppTheme.colors.background)
            .fillMaxWidth()
            .padding(top = 56.dp)
            .verticalScroll(state = rememberScrollState(), enabled = false),
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
                .width(120.dp)
                .height(20.dp)
                .shimmer()
                .align(Alignment.CenterHorizontally)
        )
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            repeat(descriptionLines) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                        .shimmer()
                )
            }
        }
        extras()
    }
}

@Composable
fun ShimmerRow(
    itemSize: Dp,
    itemCount: Int = 5,
    itemWidth: Dp = itemSize,
    itemHeight: Dp = itemSize,
    horizontalSpacing: Dp = 8.dp,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp),
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(horizontalSpacing),
        contentPadding = contentPadding,
        userScrollEnabled = false,
    ) {
        items(itemCount) {
            Box(
                modifier = Modifier
                    .width(itemWidth)
                    .height(itemHeight)
                    .shimmer()
            )
        }
    }
}
