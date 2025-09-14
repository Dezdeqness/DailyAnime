package com.dezdeqness.presentation.features.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.core.ui.views.GeneralEmpty
import com.dezdeqness.core.ui.views.GeneralError
import com.dezdeqness.presentation.features.calendar.composable.CalendarList
import com.dezdeqness.presentation.features.calendar.composable.CalendarSearch
import com.dezdeqness.presentation.features.calendar.composable.ShimmerCalendarLoading
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CalendarPage(
    modifier: Modifier = Modifier,
    stateFlow: StateFlow<CalendarState>,
    actions: CalendarActions,
) {
    val scope = rememberCoroutineScope()

    val state by stateFlow.collectAsState()

    LaunchedEffect(Unit) {
        actions.onInitialLoad()
    }

    Scaffold(
        containerColor = AppTheme.colors.onPrimary,
        modifier = modifier.fillMaxSize(),
        topBar = {
            CalendarSearch(
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                onQueryChanged = { query ->
                    actions.onQueryChanged(query)
                }
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
            if (state.isInitialLoadingIndicatorShowing) {
                ShimmerCalendarLoading(modifier = Modifier.align(Alignment.Center))
            }

            if (state.isErrorStateShowing) {
                GeneralError(modifier = Modifier.align(Alignment.Center))
            }

            if (state.list.isNotEmpty()) {
                CalendarList(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AppTheme.colors.onPrimary),
                    list = state.list,
                    isScrollNeed = state.isScrollNeed,
                    onNeedScroll = { listState ->
                        scope.launch {
                            actions.onScrolled()
                            listState.animateScrollToItem(0)
                        }
                    },
                    onActionReceive = { action ->
                        actions.onActionReceived(action)
                    }
                )
            }

            if (state.isEmptyStateShowing) {
                GeneralEmpty(modifier = Modifier.align(Alignment.Center))
            }

            PullRefreshIndicator(
                refreshing = state.isPullDownRefreshing,
                pullRefreshState,
                Modifier.align(Alignment.TopCenter)
            )

        }
    }

}
