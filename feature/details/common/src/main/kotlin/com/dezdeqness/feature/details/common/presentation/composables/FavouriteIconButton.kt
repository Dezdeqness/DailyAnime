package com.dezdeqness.feature.details.common.presentation.composables

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.dezdeqness.contract.favourite.model.FavouriteButtonState
import com.dezdeqness.foundation.ui.theme.AppTheme

@Composable
fun FavouriteIconButton(
    state: FavouriteButtonState,
    onClick: () -> Unit,
    tint: Color = AppTheme.colors.onSurface,
) {
    if (state is FavouriteButtonState.Hidden) return

    val enabled = state is FavouriteButtonState.Idle

    IconButton(onClick = onClick, enabled = enabled) {
        when (state) {
            FavouriteButtonState.Hidden -> Unit
            FavouriteButtonState.Disabled -> Icon(
                imageVector = Icons.Filled.FavoriteBorder,
                contentDescription = null,
                tint = tint.copy(alpha = 0.4f),
            )

            is FavouriteButtonState.Idle -> Icon(
                imageVector = if (state.isFavourite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                contentDescription = null,
                tint = tint,
            )

            is FavouriteButtonState.Processing -> CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = tint,
                strokeWidth = 2.dp,
            )
        }
    }
}
