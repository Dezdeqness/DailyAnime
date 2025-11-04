package com.dezdeqness.feature.favourite.presentation.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.dezdeqness.core.ui.views.image.AppImage
import com.dezdeqness.feature.favourite.presentation.models.FavouritesUiModel

@Composable
fun FavouriteCell(
    modifier: Modifier = Modifier,
    item: FavouritesUiModel
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
    ) {
        Box {
            AppImage(
                data = item.imageUrl,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .align(alignment = Alignment.Center)
            )
        }
    }
}
