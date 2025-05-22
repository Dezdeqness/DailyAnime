package com.dezdeqness.presentation.features.animelist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dezdeqness.R
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.presentation.features.animelist.composable.AnimeSearch
import com.dezdeqness.core.ui.GeneralEmpty
import com.dezdeqness.core.ui.GeneralError
import com.dezdeqness.presentation.features.animelist.composable.AnimeSearchGrid
import com.dezdeqness.presentation.features.animelist.composable.ShimmerSearchLoading
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AnimeSearchPage(
    modifier: Modifier = Modifier,
    stateFlow: StateFlow<AnimeSearchState>,
    pullRefreshFlow: StateFlow<Boolean>,
    scrollNeedFlow: StateFlow<Boolean>,
    actions: AnimeSearchActions,
) {
    val scope = rememberCoroutineScope()

    val state by stateFlow.collectAsStateWithLifecycle()

    val isPullDownRefreshing by pullRefreshFlow.collectAsStateWithLifecycle()

    val isScrollNeed by scrollNeedFlow.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = AppTheme.colors.onPrimary,
        modifier = modifier.fillMaxSize(),
        topBar = {
            AnimeSearch(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                onQueryChanged = { query ->
                    actions.onQueryChanged(query)
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                containerColor = AppTheme.colors.accent,
                contentColor = AppTheme.colors.onPrimary,
                onClick = {
                    actions.onFabClicked()
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.filter_list),
                    contentDescription = null,
                    tint = AppTheme.colors.onSurface,
                )
            }
        }
    ) { contentPadding ->
        val pullRefreshState = rememberPullRefreshState(
            refreshing = isPullDownRefreshing,
            onRefresh = {
                actions.onPullDownRefreshed()
            },
        )

        Box(
            modifier = modifier
                .padding(contentPadding)
                .fillMaxSize()
                .pullRefresh(pullRefreshState),
            contentAlignment = Alignment.Center,
        ) {
            when (state.status) {
                AnimeSearchStatus.Initial, AnimeSearchStatus.Loading -> {
                    ShimmerSearchLoading(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(horizontal = 8.dp),
                    )
                }

                AnimeSearchStatus.Error -> {
                    GeneralError(modifier = Modifier.align(Alignment.Center))
                }

                AnimeSearchStatus.Empty -> {
                    GeneralEmpty(modifier = Modifier.align(Alignment.Center))
                }

                AnimeSearchStatus.Loaded -> {
                    var isPageLoading by remember {
                        mutableStateOf(false)
                    }

                    // Workaround to fix pagination when load more was failure
                    LaunchedEffect(state.list, isPullDownRefreshing) {
                        isPageLoading = false
                    }

                    AnimeSearchGrid(
                        list = state.list,
                        hasNextPage = state.hasNextPage,
                        isPageLoading = isPageLoading,
                        isScrollNeed = isScrollNeed,
                        onLoadMore = {
                            actions.onLoadMore()
                            isPageLoading = true
                        },
                        onNeedScroll = { gridState ->
                            scope.launch {
                                actions.onScrolled()
                                gridState.animateScrollToItem(0)
                            }
                        },
                        onActionReceive = { action ->
                            actions.onActionReceived(action)
                        }
                    )
                }

            }

            PullRefreshIndicator(
                refreshing = isPullDownRefreshing,
                pullRefreshState,
                Modifier.align(Alignment.TopCenter),
            )

        }
    }

}
