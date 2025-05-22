package com.dezdeqness.presentation.features.animelist

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.dezdeqness.core.BaseComposeFragment
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.data.analytics.AnalyticsManager
import com.dezdeqness.di.AppComponent
import com.dezdeqness.presentation.action.Action
import com.dezdeqness.presentation.event.AnimeDetails
import com.dezdeqness.presentation.event.ApplyFilter
import com.dezdeqness.presentation.event.ConsumableEvent
import com.dezdeqness.presentation.event.EventConsumer
import com.dezdeqness.presentation.event.NavigateToFilter
import com.dezdeqness.presentation.features.searchfilter.AnimeSearchFilter
import com.dezdeqness.presentation.features.searchfilter.AnimeSearchFilterActions
import com.dezdeqness.presentation.features.searchfilter.AnimeSearchFilterViewModel
import com.dezdeqness.presentation.models.SearchSectionUiModel
import kotlinx.coroutines.launch
import javax.inject.Inject


class AnimeListFragment : BaseComposeFragment() {

    @Inject
    lateinit var analyticsManager: AnalyticsManager

    private val eventConsumer: EventConsumer by lazy {
        EventConsumer(
            fragment = this,
            context = this.requireContext(),
        )
    }

    private val viewModel: AnimeViewModel by viewModels(factoryProducer = { viewModelFactory })

    private val filterViewModel: AnimeSearchFilterViewModel by viewModels(factoryProducer = { viewModelFactory })

    override fun setupScreenComponent(component: AppComponent) =
        component
            .animeComponent()
            .create()
            .inject(this)

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun FragmentContent() {
        AppTheme {
            Box {
                AnimeSearchPage(
                    stateFlow = viewModel.animeSearchState,
                    pullRefreshFlow = viewModel.pullRefreshFlow,
                    scrollNeedFlow = viewModel.scrollNeedFlow,
                    actions = object : AnimeSearchActions {
                        override fun onPullDownRefreshed() {
                            viewModel.onPullDownRefreshed()
                        }

                        override fun onLoadMore() {
                            viewModel.onLoadMore()
                        }

                        override fun onActionReceived(action: Action) {
                            viewModel.onActionReceive(action = action)
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

                        override fun onScrolled() {
                            viewModel.onScrolled()
                        }

                    }
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
                            isSelected: Boolean
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

                    }
                )

            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collect { event ->
                    when (event) {
                        is NavigateToFilter -> {
                            filterViewModel.onFiltersReceived(event.filters)
                        }

                        is AnimeDetails -> {
                            findNavController().navigate(
                                AnimeListFragmentDirections.navigateToAnimeDetails(event.animeId)
                            )
                        }

                        is ConsumableEvent -> {
                            eventConsumer.consume(event)
                        }

                        else -> {}
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                filterViewModel.events.collect { event ->
                    when (event) {
                        is ApplyFilter -> {
                            viewModel.onFilterChanged(event.filters)
                        }

                        is AnimeDetails -> {
                            analyticsManager.detailsTracked(
                                id = event.animeId.toString(),
                                title = event.title
                            )
                            findNavController().navigate(
                                AnimeListFragmentDirections.navigateToAnimeDetails(event.animeId)
                            )
                        }

                        is ConsumableEvent -> {
                            eventConsumer.consume(event)
                        }

                        else -> {}
                    }
                }
            }
        }
    }

}
