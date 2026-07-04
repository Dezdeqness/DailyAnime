package com.dezdeqness.feature.details.related.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.dezdeqness.feature.details.related.R
import com.dezdeqness.feature.details.related.presentation.composables.RelatedItem
import com.dezdeqness.feature.details.related.presentation.composables.RelatedListLoading
import com.dezdeqness.feature.details.related.presentation.preview.RelatedPreviewData
import com.dezdeqness.foundation.ui.theme.AppTheme
import com.dezdeqness.foundation.ui.views.GeneralEmpty
import com.dezdeqness.foundation.ui.views.RetryError
import com.dezdeqness.foundation.ui.views.toolbar.AppToolbar
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun ChronologyListPage(
    modifier: Modifier = Modifier,
    stateFlow: StateFlow<RelatedListState>,
    actions: RelatedListActions,
) {
    RelatedListPage(
        modifier = modifier,
        title = stringResource(R.string.anime_chronology_title),
        stateFlow = stateFlow,
        actions = actions,
    )
}

@Composable
fun SimilarListPage(
    modifier: Modifier = Modifier,
    stateFlow: StateFlow<RelatedListState>,
    actions: RelatedListActions,
) {
    RelatedListPage(
        modifier = modifier,
        title = stringResource(R.string.anime_similar_title),
        stateFlow = stateFlow,
        actions = actions,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun RelatedListPage(
    modifier: Modifier = Modifier,
    title: String,
    stateFlow: StateFlow<RelatedListState>,
    actions: RelatedListActions,
) {
    val state by stateFlow.collectAsState()

    Scaffold(
        topBar = {
            AppToolbar(
                title = title,
                navigationClick = actions::onBackPressed,
            )
        },
        containerColor = AppTheme.colors.onPrimary,
    ) { padding ->
        Box(
            modifier = modifier
                .padding(padding)
                .fillMaxSize(),
        ) {
            when (state.status) {
                RelatedListStatus.Loading, RelatedListStatus.Initial -> {
                    RelatedListLoading(modifier = Modifier.fillMaxSize())
                }

                RelatedListStatus.Loaded -> {
                    RelatedList(
                        state = state,
                        actions = actions,
                    )
                }

                RelatedListStatus.Empty -> {
                    GeneralEmpty(title = stringResource(R.string.related_empty_state))
                }

                RelatedListStatus.Error -> {
                    RetryError(onRetryClick = actions::onRetryClicked)
                }
            }
        }
    }
}

@Composable
private fun RelatedList(
    modifier: Modifier = Modifier,
    state: RelatedListState,
    actions: RelatedListActions,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.colors.onPrimary),
    ) {
        items(
            count = state.list.size,
            key = { index -> state.list[index].id },
        ) { index ->
            val item = state.list[index]

            RelatedItem(
                modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp),
                item = item,
                onClick = { clicked ->
                    actions.onAnimeClicked(animeId = clicked.id, title = clicked.name)
                },
            )
        }
    }
}

@PreviewLightDark
@Composable
fun RelatedListPagePreview() {
    AppTheme {
        RelatedListPage(
            title = stringResource(R.string.anime_chronology_title),
            stateFlow = MutableStateFlow(
                RelatedListState(
                    list = RelatedPreviewData.chronologyList,
                    status = RelatedListStatus.Loaded,
                ),
            ),
            actions = RelatedPreviewData.emptyActions,
        )
    }
}

@PreviewLightDark
@Composable
fun RelatedListPageEmptyPreview() {
    AppTheme {
        RelatedListPage(
            title = stringResource(R.string.anime_chronology_title),
            stateFlow = MutableStateFlow(RelatedListState(status = RelatedListStatus.Empty)),
            actions = RelatedPreviewData.emptyActions,
        )
    }
}
