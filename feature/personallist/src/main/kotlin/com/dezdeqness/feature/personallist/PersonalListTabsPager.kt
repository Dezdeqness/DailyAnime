package com.dezdeqness.presentation.features.personallist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dezdeqness.core.ui.views.GeneralError
import com.dezdeqness.data.analytics.AnalyticsManager
import com.dezdeqness.feature.personallist.PersonalListAction
import com.dezdeqness.feature.personallist.Placeholder
import com.dezdeqness.feature.personallist.composable.PersonalList
import com.dezdeqness.feature.personallist.composable.ShimmerPersonalLoading
import com.dezdeqness.feature.personallist.composable.UserRateEmptyState
import com.dezdeqness.feature.personallist.tab.PersonalListTabViewModel
import com.dezdeqness.feature.personallist.tab.StatusIdKey
import com.dezdeqness.shared.presentation.model.RibbonStatusUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PersonalListTabsPager(
    pager: PagerState,
    ribbon: List<RibbonStatusUiModel>,
    currentSortId: String,
    viewModelFactory: ViewModelProvider.Factory,
    analyticsManager: AnalyticsManager,
    onDetailsClick: (animeId: Long) -> Unit,
    onOpenEditRate: (userRateId: Long, title: String) -> Unit,
    refreshStatusFlow: Flow<String>,
) {
    val scope = rememberCoroutineScope()

    HorizontalPager(
        state = pager,
        modifier = Modifier.fillMaxSize(),
    ) { page ->
        val statusId = ribbon.getOrNull(page)?.id ?: return@HorizontalPager

        val owner = LocalViewModelStoreOwner.current

        val extras = if (owner is HasDefaultViewModelProviderFactory) {
            owner.defaultViewModelCreationExtras
        } else {
            CreationExtras.Empty
        }

        val tabViewModel = viewModel<PersonalListTabViewModel>(
            key = "personal_list_tab_$statusId",
            factory = viewModelFactory,
            extras = MutableCreationExtras(extras).apply {
                set(StatusIdKey, statusId)
            }
        )

        val tabState by tabViewModel.stateFlow.collectAsStateWithLifecycle()

        val currentSortIdState by rememberUpdatedState(currentSortId)

        LaunchedEffect(statusId) {
            tabViewModel.setSort(currentSortIdState)
            tabViewModel.onInitialLoad()
        }

        LaunchedEffect(currentSortIdState) {
            tabViewModel.setSort(currentSortIdState)
        }

        LaunchedEffect(statusId, tabViewModel) {
            refreshStatusFlow.collectLatest { refreshedStatus ->
                if (refreshedStatus == statusId) {
                    tabViewModel.refreshRemote()
                }
            }
        }

        val pullRefreshState = rememberPullRefreshState(
            refreshing = tabState.isPullDownRefreshing,
            onRefresh = {
                tabViewModel.onPullDownRefreshed()
            },
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState),
        ) {
            if (tabState.isLoadingStateShowing) {
                ShimmerPersonalLoading(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                )
            }

            when (tabState.placeholder) {
                Placeholder.UserRate.Error -> GeneralError(modifier = Modifier.fillMaxSize())
                Placeholder.UserRate.Empty -> UserRateEmptyState(modifier = Modifier.fillMaxSize())
                else -> {}
            }

            PersonalList(
                list = tabState.items,
                isScrollNeed = tabState.isScrollNeed,
                onActionReceive = { action ->
                    when (action) {
                        is PersonalListAction.AnimeClick -> {
                            analyticsManager.detailsTracked(
                                id = action.animeId.toString(),
                                title = action.title,
                            )
                            onDetailsClick(action.animeId)
                        }

                        is PersonalListAction.EditRateClicked -> {
                            val title = tabViewModel.getUserRateTitle(action.editRateId).orEmpty()
                            onOpenEditRate(action.editRateId, title)
                        }

                        is PersonalListAction.UserRateIncrement -> {
                            tabViewModel.onUserRateIncrement(action.editRateId)
                        }
                    }
                },
                onNeedScroll = { listState ->
                    scope.launch {
                        tabViewModel.onScrolled()
                        listState.animateScrollToItem(0)
                    }
                },
                modifier = Modifier.fillMaxSize(),
            )

            PullRefreshIndicator(
                refreshing = tabState.isPullDownRefreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter),
            )
        }
    }
}
