package com.dezdeqness.feature.details.common.presentation

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dezdeqness.feature.details.common.presentation.composables.DetailsScaffold
import com.dezdeqness.feature.details.common.presentation.composables.DetailsToolbar
import com.dezdeqness.feature.details.common.presentation.store.DetailsState
import com.dezdeqness.feature.details.common.presentation.store.DetailsStatus
import kotlinx.coroutines.flow.StateFlow

@Composable
fun <UiEvent : Any, State : DetailsState> DetailsPage(
    stateFlow: StateFlow<State>,
    onUiEvent: (UiEvent) -> Unit,
    onShare: () -> Unit,
    onRetry: () -> Unit,
    onBack: () -> Unit,
    onFavouriteClick: () -> Unit,
    modifier: Modifier = Modifier,
    overlay: @Composable BoxScope.(State, (UiEvent) -> Unit) -> Unit = { _, _ ->},
    loading: @Composable (Modifier) -> Unit,
) {
    val state by stateFlow.collectAsStateWithLifecycle()

    DetailsScaffold(
        modifier = modifier,
        status = state.status,
        onRetryClick = onRetry,
        loading = { loading(Modifier.fillMaxSize()) },
        content = { listState ->
            DetailsContent(
                sections = state.sections,
                onEvent = onUiEvent,
                listState = listState,
                modifier = Modifier.fillMaxSize(),
            )
        },
        toolbar = { toolbarColor ->
            DetailsToolbar(
                isLoaded = state.status == DetailsStatus.Loaded,
                toolbarColor = toolbarColor,
                favouriteState = state.favouriteButton,
                onBackClick = onBack,
                onShareClick = onShare,
                onFavouriteClick = onFavouriteClick,
            )
        },
        overlay = { overlay(state, onUiEvent) },
    )
}
