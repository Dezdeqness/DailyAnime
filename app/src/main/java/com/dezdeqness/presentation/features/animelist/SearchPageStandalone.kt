package com.dezdeqness.presentation.features.animelist

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.dezdeqness.ShikimoriApp
import com.dezdeqness.contract.filter.model.SearchSectionUiModel
import com.dezdeqness.feature.search.presentation.AnimeSearchActions
import com.dezdeqness.feature.search.presentation.AnimeSearchPage
import com.dezdeqness.feature.search.presentation.AnimeViewModel
import com.dezdeqness.feature.searchfilter.presentation.AnimeSearchFilter
import com.dezdeqness.feature.searchfilter.presentation.AnimeSearchFilterActions
import com.dezdeqness.feature.searchfilter.presentation.AnimeSearchFilterViewModel
import com.dezdeqness.foundation.utils.collectEvents
import com.dezdeqness.presentation.AnimeDetails

@Composable
fun SearchPageStandalone(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    val context = LocalContext.current
    val animeComponent = remember {
        (context.applicationContext as ShikimoriApp).appComponent
            .animeComponent()
            .create()
    }

    val analyticsManager = animeComponent.analyticsManager()

    val viewModel = viewModel<AnimeViewModel>(factory = animeComponent.viewModelFactory())
    val filterViewModel =
        viewModel<AnimeSearchFilterViewModel>(factory = animeComponent.viewModelFactory())

    Box(modifier = modifier) {
        AnimeSearchPage(
            stateFlow = viewModel.animeSearchState,
            pullRefreshFlow = viewModel.pullRefreshFlow,
            scrollNeedFlow = viewModel.scrollNeedFlow,
            isListScrollingFlow = viewModel.isListScrolling,
            historySearchFlow = viewModel.historySearchFlow,
            actions = object : AnimeSearchActions {
                override fun onPullDownRefreshed() {
                    viewModel.onPullDownRefreshed()
                }

                override fun onLoadMore() {
                    viewModel.onLoadMore()
                }

                override fun onAnimeClicked(animeId: Long, title: String) {
                    analyticsManager.detailsTracked(
                        id = animeId.toString(),
                        title = title,
                    )
                    navController.navigate(AnimeDetails(animeId))
                }

                override fun onFabClicked() {
                    viewModel.onFabClicked()
                }

                override fun onQueryChanged(query: String) {
                    viewModel.onQueryChanged(query)
                }

                override fun onFilterChanged(filtersList: List<SearchSectionUiModel>) {
                    viewModel.onFilterChanged(filtersList = filtersList)
                }

                override fun onScrollInProgress(isScrollInProgress: Boolean) {
                    viewModel.onScrollInProgress(isScrollInProgress)
                }

                override fun removeSearchHistoryItem(item: String) {
                    viewModel.onRemoveSearchHistoryItem(item)
                }

                override fun onScrolled() {
                    viewModel.onScrolled()
                }
            },
        )

        AnimeSearchFilter(
            stateFlow = filterViewModel.animeSearchFilterStateFlow,
            actions = object : AnimeSearchFilterActions {
                override fun onDismissed() {
                    filterViewModel.onDismissed()
                }

                override fun onCellClicked(
                    innerId: String,
                    cellId: String,
                    isSelected: Boolean,
                ) {
                    filterViewModel.onCellClicked(
                        innerId = innerId,
                        cellId = cellId,
                        isSelected = isSelected,
                    )
                }

                override fun onApplyFilter() {
                    filterViewModel.onApplyButtonClicked()
                }

                override fun onResetFilter() {
                    filterViewModel.onResetButtonClicked()
                }
            },
        )
    }

    viewModel.navigateToFilter.collectEvents { filters ->
        filterViewModel.onFiltersReceived(filters)
    }

    filterViewModel.appliedFilters.collectEvents { filters ->
        viewModel.onFilterChanged(filters)
    }
}
