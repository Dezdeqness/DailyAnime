package com.dezdeqness.presentation.features.animedetails

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.dezdeqness.R
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.presentation.features.animedetails.composables.DetailsList
import com.dezdeqness.presentation.features.animedetails.composables.DetailsToolbar
import com.dezdeqness.presentation.features.animedetails.composables.ShimmerDetailsLoading
import com.dezdeqness.presentation.features.animedetails.composables.list.DetailsError
import kotlinx.coroutines.flow.StateFlow

@Composable
fun DetailsPage(
    modifier: Modifier = Modifier,
    stateFlow: StateFlow<AnimeDetailsState>,
    actions: DetailsActions,
) {
    val state by stateFlow.collectAsState()

    val listState = rememberLazyListState()
    val scrollIndex by remember { derivedStateOf { listState.firstVisibleItemIndex } }
    val isScrolledPast = remember(scrollIndex) { scrollIndex >= 1 }

    val toolbarBackgroundColor =
        if (isScrolledPast) AppTheme.colors.onPrimary else Color.Transparent

    Box(modifier = modifier.fillMaxSize()) {

        when (state.status) {
            DetailsStatus.Loading, DetailsStatus.Initial -> {
                ShimmerDetailsLoading(modifier = Modifier.fillMaxSize())
            }

            DetailsStatus.Loaded -> {
                DetailsList(
                    list = state.uiModels,
                    state = listState,
                    onClick = { action -> actions.onActionReceive(action) }
                )
            }

            DetailsStatus.Error -> {
                DetailsError(onRetryClick = actions::onRetryClicked)
            }
        }

        DetailsToolbar(
            isLoaded = state.uiModels.isNotEmpty(),
            toolbarColor = toolbarBackgroundColor,
            onBackClick = actions::onBackPressed,
            onShareClick = actions::onSharePressed,
        )

        if (state.isEditRateFabShown) {
            FloatingActionButton(
                onClick = actions::onFabClicked,
                containerColor = colorResource(id = R.color.purple_500),
                contentColor = AppTheme.colors.onPrimary,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Icon(
                    painterResource(R.drawable.ic_edit),
                    contentDescription = null,
                    tint = colorResource(id = R.color.pure_white),
                )
            }
        }
    }
}
