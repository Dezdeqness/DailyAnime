package com.dezdeqness.feature.details.common.presentation.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.dezdeqness.contract.favourite.model.FavouriteButtonState
import com.dezdeqness.foundation.ui.theme.AppTheme
import com.dezdeqness.foundation.ui.views.toolbar.AppToolbar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsToolbar(
    modifier: Modifier = Modifier,
    toolbarColor: Color = AppTheme.colors.onPrimary,
    isLoaded: Boolean,
    favouriteState: FavouriteButtonState = FavouriteButtonState.Hidden,
    onBackClick: () -> Unit,
    onShareClick: () -> Unit,
    onFavouriteClick: () -> Unit = {},
) {
    AppToolbar(
        modifier = modifier,
        title = "",
        navigationClick = onBackClick,
        colors = TopAppBarDefaults.topAppBarColors().copy(containerColor = toolbarColor),
        actions = {
            if (isLoaded) {
                FavouriteIconButton(state = favouriteState, onClick = onFavouriteClick)
                IconButton(onClick = onShareClick) {
                    Icon(
                        imageVector = Icons.Filled.Share,
                        contentDescription = null,
                        tint = AppTheme.colors.onSurface,
                    )
                }
            }
        },
    )
}
