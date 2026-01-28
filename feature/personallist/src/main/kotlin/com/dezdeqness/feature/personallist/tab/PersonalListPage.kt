package com.dezdeqness.feature.personallist.tab

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dezdeqness.core.ui.views.GeneralError
import com.dezdeqness.feature.personallist.composable.PersonalList
import com.dezdeqness.feature.personallist.composable.ShimmerPersonalLoading
import com.dezdeqness.feature.personallist.composable.UserRateEmptyState
import com.dezdeqness.feature.personallist.tab.store.PersonalListNamespace
import com.dezdeqness.feature.personallist.tab.store.PersonalListStatus
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PersonalListPage(
    stateFlow: StateFlow<PersonalListNamespace.State>,
    actions: PersonalListActions,
) {
    val state by stateFlow.collectAsStateWithLifecycle()

    val pullRefreshState = rememberPullRefreshState(
        refreshing = state.isPullDownRefreshing,
        onRefresh = {
            actions.onPullDownRefreshed()
        },
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState),
    ) {
        when (state.dataStatus) {
            PersonalListStatus.Error -> GeneralError(modifier = Modifier.fillMaxSize())
            PersonalListStatus.Empty -> UserRateEmptyState(modifier = Modifier.fillMaxSize())
            PersonalListStatus.Loading -> ShimmerPersonalLoading(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
            )

            else -> {}
        }

        var isPageLoading by remember {
            mutableStateOf(false)
        }

        // Workaround to fix pagination when load more was failure
        LaunchedEffect(state.list, state.isPullDownRefreshing) {
            isPageLoading = false
        }

        PersonalList(
            list = state.list,
            onActionReceive = { action ->
                when (action) {
                    is PersonalListAction.AnimeClick -> {
                        actions.onAnimeClicked(action.animeId, action.title)
                    }

                    is PersonalListAction.EditRateClicked -> {
                        actions.onOpenEditRateClicked(action.editRateId, action.displayName)
                    }

                    is PersonalListAction.UserRateIncrement -> {
                        actions.onUserRateIncrement(action.editRateId)
                    }
                }
            },
            hasNextPage = state.hasNextPage,
            isPageLoading = isPageLoading,
            onLoadMore = {
                actions.onLoadMore()
                isPageLoading = true
            },
            modifier = Modifier.fillMaxSize(),
        )

        PullRefreshIndicator(
            refreshing = state.isPullDownRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
        )
    }
}
