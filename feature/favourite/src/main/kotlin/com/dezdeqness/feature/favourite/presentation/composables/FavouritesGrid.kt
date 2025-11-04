package com.dezdeqness.feature.favourite.presentation.composables

import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.content.res.Configuration.ORIENTATION_UNDEFINED
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.dezdeqness.feature.favourite.presentation.models.FavouritesUiModel

private const val PORTRAIT_MAX_CELLS = 3
private const val LANDSCAPE_MAX_CELLS = 6

@Composable
fun FavouritesGrid(
    modifier: Modifier = Modifier,
    items: List<FavouritesUiModel>,
) {
    val gridState = rememberLazyGridState()
    val configuration = LocalConfiguration.current

    val cellCount = remember(configuration.orientation) {
        if (configuration.orientation == ORIENTATION_PORTRAIT
            || configuration.orientation == ORIENTATION_UNDEFINED
        ) {
            PORTRAIT_MAX_CELLS
        } else {
            LANDSCAPE_MAX_CELLS
        }
    }
    LazyVerticalGrid(
        state = gridState,
        columns = GridCells.Fixed(cellCount),
        modifier = modifier.fillMaxSize(),
    ) {
        items(
            count = items.size,
            key = { index ->
                val item = items[index]
                item.id + item.title.hashCode()
            }
        ) { index ->
            val item = items[index]

            val padding = calculateItemPadding(index, cellCount)

            FavouriteCell(
                item = item,
                modifier = Modifier
                    .animateItem()
                    .padding(padding),
            )
        }
    }
}

private fun calculateItemPadding(index: Int, cellCount: Int): PaddingValues {
    val column = index % cellCount
    val left = 8.dp - column * 8.dp / cellCount
    val right = (column + 1) * 8.dp / cellCount
    return PaddingValues(start = left, end = right, top = 8.dp)
}
