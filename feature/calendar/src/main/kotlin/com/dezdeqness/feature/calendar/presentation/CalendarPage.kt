package com.dezdeqness.feature.calendar.presentation

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.dezdeqness.feature.calendar.presentation.composables.CalendarList
import com.dezdeqness.feature.calendar.presentation.composables.CalendarSearch
import com.dezdeqness.feature.calendar.presentation.composables.ShimmerCalendarLoading
import com.dezdeqness.feature.calendar.presentation.preview.CalendarPreviewData
import com.dezdeqness.foundation.ui.theme.AppTheme
import com.dezdeqness.foundation.ui.views.GeneralEmpty
import com.dezdeqness.foundation.ui.views.GeneralError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CalendarPage(
    modifier: Modifier = Modifier,
    stateFlow: StateFlow<CalendarState>,
    pullRefreshFlow: StateFlow<Boolean>,
    scrollNeedFlow: StateFlow<Boolean>,
    actions: CalendarActions,
) {
    val scope = rememberCoroutineScope()

    val state by stateFlow.collectAsState()
    val isPullDownRefreshing by pullRefreshFlow.collectAsState()
    val isScrollNeed by scrollNeedFlow.collectAsState()

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
                },
            )
        },
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
                CalendarStatus.Initial -> {
                    ShimmerCalendarLoading(modifier = Modifier.align(Alignment.Center))
                }

                CalendarStatus.Error -> {
                    GeneralError(modifier = Modifier.align(Alignment.Center))
                }

                CalendarStatus.Empty -> {
                    GeneralEmpty(modifier = Modifier.align(Alignment.Center))
                }

                CalendarStatus.Loaded -> {
                    CalendarList(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(AppTheme.colors.onPrimary),
                        list = state.list,
                        isScrollNeed = isScrollNeed,
                        onNeedScroll = { listState ->
                            scope.launch {
                                actions.onScrolled()
                                listState.animateScrollToItem(0)
                            }
                        },
                        onAnimeClicked = actions::onAnimeClicked,
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

@PreviewLightDark
@Composable
fun CalendarPagePreview() {
    AppTheme {
        CalendarPage(
            stateFlow = MutableStateFlow(
                CalendarState(
                    list = CalendarPreviewData.list,
                    status = CalendarStatus.Loaded,
                ),
            ),
            pullRefreshFlow = MutableStateFlow(false),
            scrollNeedFlow = MutableStateFlow(false),
            actions = CalendarPreviewData.emptyActions,
        )
    }
}

@PreviewLightDark
@Composable
fun CalendarPageLoadingPreview() {
    AppTheme {
        CalendarPage(
            stateFlow = MutableStateFlow(CalendarState(status = CalendarStatus.Initial)),
            pullRefreshFlow = MutableStateFlow(false),
            scrollNeedFlow = MutableStateFlow(false),
            actions = CalendarPreviewData.emptyActions,
        )
    }
}
