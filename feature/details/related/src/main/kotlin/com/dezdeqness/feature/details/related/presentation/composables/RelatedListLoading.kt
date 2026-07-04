package com.dezdeqness.feature.details.related.presentation.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dezdeqness.foundation.ui.views.shimmer

@Composable
fun RelatedListLoading(
    modifier: Modifier = Modifier,
    times: Int = 8,
) {
    Column(
        modifier = modifier
            .verticalScroll(state = rememberScrollState(), enabled = false)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        repeat(times) { _ ->
            ShimmerItem()
        }
    }
}

@Composable
private fun ShimmerItem(modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .shimmer(),
        )
    }
}
