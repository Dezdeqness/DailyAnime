package com.dezdeqness.feature.home.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.dezdeqness.feature.history.presentation.composables.HistoryShimmerItem
import com.dezdeqness.feature.home.presentation.composables.HistoryLatestSection
import com.dezdeqness.feature.home.presentation.composables.HomeBanner
import com.dezdeqness.feature.home.presentation.composables.HomeCalendarSection
import com.dezdeqness.feature.home.presentation.composables.HomeSection
import com.dezdeqness.feature.home.presentation.composables.ShimmerHomeLoading
import com.dezdeqness.feature.home.presentation.models.SectionStatus
import com.dezdeqness.feature.home.presentation.preview.HomePreviewData
import com.dezdeqness.foundation.ui.theme.AppTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun HomePage(
    modifier: Modifier = Modifier,
    stateFlow: StateFlow<HomeState>,
    actions: HomeActions,
) {
    val state by stateFlow.collectAsState()

    LaunchedEffect(Unit) {
        actions.onInitialLoad()
    }

    val genreSections = state.sectionsState.genreSections
    val calendarSection = state.sectionsState.calendarSection
    val authorizedState = state.authorizedState
    val latestHistory = state.sectionsState.latestHistoryItem

    val isLoadingVisible = remember(genreSections, calendarSection) {
        genreSections
            .map { it.status }
            .any { it == SectionStatus.Initial || it == SectionStatus.Loading } &&
            calendarSection.status == SectionStatus.Initial ||
            calendarSection.status == SectionStatus.Loading
    }

    val isEmptyContent = remember(genreSections, calendarSection) {
        genreSections.map { it.items }.all { it.isEmpty() } &&
            calendarSection.items.isEmpty()
    }

    LazyColumn(
        modifier = modifier
            .background(AppTheme.colors.onPrimary)
            .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
            .fillMaxSize(),
        userScrollEnabled = isLoadingVisible.not(),
    ) {
        item {
            HomeBanner(
                authorizedState = authorizedState,
            )
        }

        if (isLoadingVisible || isEmptyContent) {
            item {
                ShimmerHomeLoading()
            }
        } else {
            if (calendarSection.items.isNotEmpty()) {
                item {
                    HomeCalendarSection(
                        items = calendarSection.items,
                        isCalendarActionVisible = calendarSection.isCalendarActionVisible,
                        onCalendarHeaderClicked = actions::onCalendarHeaderClicked,
                        onAnimeClicked = actions::onAnimeClicked,
                    )
                }
            }

            if (latestHistory.status == SectionStatus.Loading) {
                item {
                    HistoryShimmerItem(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .padding(top = 8.dp),
                    )
                }
            } else {
                latestHistory.historyUiModel?.let {
                    item {
                        HistoryLatestSection(
                            modifier = Modifier.padding(vertical = 8.dp).animateItem(),
                            item = it,
                            onHistoryHeaderClicked = actions::onHistoryHeaderClicked,
                        )
                    }
                }
            }

            items(
                count = genreSections.size,
                key = { index -> genreSections[index].toString() },
            ) { index ->
                val section = genreSections[index]
                HomeSection(
                    title = section.title,
                    items = section.items,
                    status = section.status,
                    onAnimeClicked = actions::onAnimeClicked,
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
fun HomePagePreview() {
    AppTheme {
        HomePage(
            stateFlow = MutableStateFlow(HomePreviewData.loadedState),
            actions = HomePreviewData.emptyActions,
        )
    }
}

@PreviewLightDark
@Composable
fun HomePageLoadingPreview() {
    AppTheme {
        HomePage(
            stateFlow = MutableStateFlow(HomePreviewData.loadingState),
            actions = HomePreviewData.emptyActions,
        )
    }
}
