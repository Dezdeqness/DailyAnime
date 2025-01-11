package com.dezdeqness.presentation.features.animelist

import android.os.Bundle
import android.view.View
import androidx.compose.runtime.Composable
import androidx.fragment.app.clearFragmentResultListener
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.dezdeqness.core.BaseComposeFragment
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.di.AppComponent
import com.dezdeqness.presentation.event.NavigateToFilter
import com.dezdeqness.presentation.action.Action
import com.dezdeqness.presentation.event.AnimeDetails
import com.dezdeqness.presentation.event.ConsumableEvent
import com.dezdeqness.presentation.event.EventConsumer
import com.dezdeqness.presentation.features.searchfilter.anime.AnimeSearchFilterBottomSheetDialog
import com.dezdeqness.presentation.features.searchfilter.anime.AnimeSearchFilterBottomSheetDialog.Companion.TAG
import com.dezdeqness.presentation.models.AnimeSearchFilter
import kotlinx.coroutines.launch


class AnimeListFragment : BaseComposeFragment() {

    private val eventConsumer: EventConsumer by lazy {
        EventConsumer(
            fragment = this,
            context = this.requireContext(),
        )
    }

    private val viewModel: AnimeViewModel by viewModels(factoryProducer = { viewModelFactory })

    override fun setupScreenComponent(component: AppComponent) =
        component
            .animeComponent()
            .create()
            .inject(this)

    @Composable
    override fun FragmentContent() {
        AppTheme {
            AnimeSearchPage(
                stateFlow = viewModel.animeSearchStateFlow,
                actions = object : AnimeSearchActions {
                    override fun onPullDownRefreshed() {
                        viewModel.onPullDownRefreshed()
                    }

                    override fun onLoadMore() {
                        viewModel.onLoadMore()
                    }

                    override fun onInitialLoad() {
                        viewModel.onInitialLoad()
                    }

                    override fun onActionReceived(action: Action) {
                        viewModel.onActionReceive(action = action)
                    }

                    override fun onFabClicked() {
                        if (parentFragmentManager.findFragmentByTag(TAG) == null) {
                            viewModel.onFabClicked()
                        }
                    }

                    override fun onQueryChanged(query: String) {
                        viewModel.onQueryChanged(query)
                    }

                    override fun onFilterChanged(filtersList: List<AnimeSearchFilter>) {
                        viewModel.onFilterChanged(filtersList = filtersList)
                    }

                    override fun onScrolled() {
                        viewModel.onScrolled()
                    }

                }
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(TAG) { _, bundle ->
            val filtersList =
                bundle.getParcelableArrayList<AnimeSearchFilter>(AnimeSearchFilterBottomSheetDialog.RESULT)
                    .orEmpty()
            viewModel.onFilterChanged(filtersList)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
    }

    override fun onDestroy() {
        super.onDestroy()
        clearFragmentResultListener(TAG)
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collect { event ->
                    when (event) {
                        is NavigateToFilter -> {
                            val dialog =
                                AnimeSearchFilterBottomSheetDialog.newInstance(event.filters)
                            dialog.show(parentFragmentManager, TAG)
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
    }

}
