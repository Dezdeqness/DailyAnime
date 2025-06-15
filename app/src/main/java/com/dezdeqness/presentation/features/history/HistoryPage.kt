package com.dezdeqness.presentation.features.history

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dezdeqness.R
import com.dezdeqness.core.ui.GeneralEmpty
import com.dezdeqness.core.ui.GeneralError
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.core.ui.views.toolbar.AppToolbar
import com.dezdeqness.presentation.features.history.composables.HistoryList
import com.dezdeqness.presentation.features.history.composables.HistoryShimmerLoading
import com.dezdeqness.presentation.features.history.store.HistoryNamespace
import com.dezdeqness.presentation.features.history.store.HistoryStatus
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun HistoryPage(
    modifier: Modifier = Modifier,
    stateFlow: StateFlow<HistoryNamespace.State>,
    actions: HistoryActions,
) {
    val state by stateFlow.collectAsState()

    Scaffold(
        containerColor = AppTheme.colors.onPrimary,
        modifier = modifier.fillMaxSize(),
        topBar = {
            AppToolbar(
                title = stringResource(R.string.history_toolbar_title),
                navigationClick = actions::onBackPressed,
            )
        },
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
            when (state.status) {
                HistoryStatus.Initial, HistoryStatus.Loading -> {
                    HistoryShimmerLoading(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        times = 10,
                    )
                }
                HistoryStatus.Error -> {
                    GeneralError(modifier = Modifier.align(Alignment.Center))

                }
                HistoryStatus.Empty -> {
                    GeneralEmpty(modifier = Modifier.align(Alignment.Center))
                }
                HistoryStatus.Loaded -> {
                    var isPageLoading by remember {
                        mutableStateOf(false)
                    }

                    // Workaround to fix pagination when load more was failure
                    LaunchedEffect(state.list, state.isPullDownRefreshing) {
                        isPageLoading = false
                    }

                    HistoryList(
                        list = state.list,
                        hasNextPage = state.hasNextPage,
                        isPageLoading = isPageLoading,
                        onLoadMore = {
                            actions.onLoadMore()
                            isPageLoading = true
                        },
                    )
                }
            }

            PullRefreshIndicator(
                refreshing = state.isPullDownRefreshing,
                pullRefreshState,
                Modifier.align(Alignment.TopCenter)
            )

        }
    }

}
