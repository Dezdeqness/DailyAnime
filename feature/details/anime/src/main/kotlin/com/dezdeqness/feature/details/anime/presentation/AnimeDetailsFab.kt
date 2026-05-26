package com.dezdeqness.feature.details.anime.presentation

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dezdeqness.feature.details.anime.R
import com.dezdeqness.feature.details.anime.presentation.store.AnimeDetailsNamespace
import com.dezdeqness.feature.details.common.presentation.store.DetailsStatus
import com.dezdeqness.foundation.ui.theme.AppTheme

@Composable
fun BoxScope.AnimeDetailsFab(
    state: AnimeDetailsNamespace.State,
    onEvent: (AnimeDetailsUiEvent) -> Unit,
) {
    if (state.isAuthorized && state.status == DetailsStatus.Loaded) {
        FloatingActionButton(
            onClick = { onEvent(AnimeDetailsUiEvent.EditRateClicked) },
            containerColor = AppTheme.colors.accent,
            contentColor = AppTheme.colors.white,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .windowInsetsPadding(WindowInsets.navigationBars),
        ) {
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = stringResource(R.string.anime_details_fab_edit_rate),
                tint = AppTheme.colors.white,
            )
        }
    }
}
