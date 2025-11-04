package com.dezdeqness.feature.favourite.presentation.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dezdeqness.core.ui.views.shimmer

@Composable
fun FavouritesLoading(
    modifier: Modifier = Modifier,
    times: Int = 4,
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState(), enabled = false)
    ) {
        repeat(times) { _ ->
            ShimmerFavouritePair()
        }
    }
}

@Composable
private fun ShimmerFavouritePair(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        repeat(3) {
            ShimmerFavouriteItem(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun ShimmerFavouriteItem(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .padding(bottom = 8.dp)
            .fillMaxWidth()
            .height(150.dp)
            .shimmer()
    )
}
