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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.dezdeqness.R
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.presentation.features.animelist.composable.AnimeSearch
import com.dezdeqness.core.ui.GeneralEmpty
import com.dezdeqness.core.ui.GeneralError
import com.dezdeqness.presentation.features.animelist.composable.AnimeSearchGrid
import com.dezdeqness.presentation.features.animelist.composable.ShimmerSearchLoading
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AnimeSearchPage(
    modifier: Modifier = Modifier,
    state: AnimeSearchState,
    actions: AnimeSearchActions,
) {
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        actions.onInitialLoad()
    }

    Scaffold(
        containerColor = colorResource(id = R.color.background_tint),
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
                containerColor = colorResource(id = R.color.purple_500),
                contentColor = AppTheme.colors.onPrimary,
                onClick = {
                    actions.onFabClicked()
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.filter_list),
                    contentDescription = null,
                    tint = colorResource(id = R.color.pure_white),
                )
            }
        }
    ) { contentPadding ->
        val pullRefreshState = rememberPullRefreshState(
            refreshing = state.isPullDownRefreshing,
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
            if (state.isLoadingStateShowing) {
                ShimmerSearchLoading(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 8.dp),
                )
            }

            if (state.isErrorStateShowing) {
                GeneralError(modifier = Modifier.align(Alignment.Center))
            }

            if (state.isEmptyStateShowing) {
                GeneralEmpty(modifier = Modifier.align(Alignment.Center))
            }

            if (state.list.isNotEmpty()) {

                var isPageLoading by remember {
                    mutableStateOf(false)
                }

                // Workaround to fix pagination when load more was failure
                LaunchedEffect(state.list, state.isPullDownRefreshing) {
                    isPageLoading = false
                }

                AnimeSearchGrid(
                    list = state.list,
                    hasNextPage = state.hasNextPage,
                    isPageLoading = isPageLoading,
                    isScrollNeed = state.isScrollNeed,
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

            PullRefreshIndicator(
                refreshing = state.isPullDownRefreshing,
                pullRefreshState,
                Modifier.align(Alignment.TopCenter)
            )

        }
    }

}
