package com.dezdeqness.feature.details.anime.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dezdeqness.feature.details.anime.R
import com.dezdeqness.feature.details.anime.presentation.composables.AnimeDetailsContent
import com.dezdeqness.feature.details.anime.presentation.store.AnimeDetailsNamespace
import com.dezdeqness.feature.details.common.presentation.composables.DetailsLoadingSkeleton
import com.dezdeqness.feature.details.common.presentation.composables.DetailsScaffold
import com.dezdeqness.feature.details.common.presentation.composables.DetailsToolbar
import com.dezdeqness.feature.details.common.presentation.composables.ShimmerRow
import com.dezdeqness.feature.details.common.presentation.store.DetailsStatus
import com.dezdeqness.foundation.ui.theme.AppTheme
import kotlinx.coroutines.flow.StateFlow

@Composable
fun AnimeDetailsPage(
    modifier: Modifier = Modifier,
    stateFlow: StateFlow<AnimeDetailsNamespace.State>,
    actions: AnimeDetailsActions,
) {
    val state by stateFlow.collectAsStateWithLifecycle()

    DetailsScaffold(
        modifier = modifier,
        status = state.status,
        onRetryClick = actions::onRetryClicked,
        loading = {
            DetailsLoadingSkeleton(
                modifier = Modifier.fillMaxSize(),
            ) {
                ShimmerRow(
                    itemSize = 60.dp,
                    itemWidth = 60.dp,
                    itemHeight = 20.dp,
                    contentPadding = PaddingValues(horizontal = 16.dp),
                )
                ShimmerRow(
                    itemSize = 120.dp,
                    itemWidth = 120.dp,
                    itemHeight = 170.dp,
                    contentPadding = PaddingValues(horizontal = 16.dp),
                )
            }
        },
        content = { listState ->
            AnimeDetailsContent(
                sections = state.sections,
                actions = actions,
                state = listState,
            )
        },
        toolbar = { toolbarColor ->
            DetailsToolbar(
                isLoaded = state.sections.isNotEmpty(),
                toolbarColor = toolbarColor,
                onBackClick = actions::onBackPressed,
                onShareClick = actions::onSharePressed,
            )
        },
        overlay = {
            if (state.isAuthorized && state.status == DetailsStatus.Loaded) {
                FloatingActionButton(
                    onClick = actions::onEditRateClicked,
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
        },
    )
}
