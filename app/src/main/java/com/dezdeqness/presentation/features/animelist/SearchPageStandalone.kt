package com.dezdeqness.presentation.features.animelist

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.dezdeqness.contract.filter.model.SearchSectionUiModel
import com.dezdeqness.di.source.SearchSourceComponent
import com.dezdeqness.feature.search.presentation.AnimeSearchActions
import com.dezdeqness.feature.search.presentation.AnimeSearchPage
import com.dezdeqness.feature.search.presentation.AnimeViewModel
import com.dezdeqness.feature.search.presentation.history.SearchHistoryViewModel
import com.dezdeqness.feature.search.presentation.store.SearchNamespace
import com.dezdeqness.feature.searchfilter.presentation.AnimeSearchFilter
import com.dezdeqness.feature.searchfilter.presentation.AnimeSearchFilterActions
import com.dezdeqness.feature.searchfilter.presentation.AnimeSearchFilterViewModel
import com.dezdeqness.foundation.utils.collectEvents
import com.dezdeqness.presentation.AnimeDetails
import com.dezdeqness.sourceComponent

@Composable
fun SearchPageStandalone(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    sourceComponent: SearchSourceComponent = LocalContext.current.sourceComponent,
) {
    val animeComponent = remember(sourceComponent) {
        sourceComponent
            .animeComponent()
            .create()
    }

    val analyticsManager = animeComponent.analyticsManager()

    val viewModel = viewModel<AnimeViewModel>(factory = animeComponent.viewModelFactory())
    val filterViewModel =
        viewModel<AnimeSearchFilterViewModel>(factory = animeComponent.viewModelFactory())
    val historyViewModel = viewModel<SearchHistoryViewModel>(factory = animeComponent.viewModelFactory())

    Box(modifier = modifier) {
        AnimeSearchPage(
            stateFlow = viewModel.uiState,
            historyFlow = historyViewModel.history,
            scrollToTopRequests = viewModel.scrollToTopRequests,
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
                    historyViewModel.onQuerySubmitted(query)
                }

                override fun onFilterChanged(filtersList: List<SearchSectionUiModel>) {
                    viewModel.onFilterChanged(filtersList)
                }

                override fun removeSearchHistoryItem(item: String) {
                    historyViewModel.onRemoveClicked(item)
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

    viewModel.uiEffects.collectEvents { effect ->
        if (effect is SearchNamespace.Effect.OpenFilters) {
            filterViewModel.onFiltersReceived(effect.filters)
        }
    }

    filterViewModel.appliedFilters.collectEvents { filters ->
        viewModel.onFilterChanged(filters)
    }
}
