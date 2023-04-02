package com.dezdeqness.presentation.features.animelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.clearFragmentResultListener
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.dezdeqness.R
import com.dezdeqness.core.BackFragmentListener
import com.dezdeqness.core.BaseFragment
import com.dezdeqness.databinding.FragmentAnimeListBinding
import com.dezdeqness.databinding.ItemSearchViewBinding
import com.dezdeqness.di.AppComponent
import com.dezdeqness.presentation.event.NavigateToFilter
import com.dezdeqness.presentation.event.ScrollToTop
import com.dezdeqness.presentation.action.Action
import com.dezdeqness.presentation.action.ActionListener
import com.dezdeqness.presentation.event.ConsumableEvent
import com.dezdeqness.presentation.event.EventConsumer
import com.dezdeqness.presentation.features.searchfilter.anime.AnimeSearchFilterBottomSheetDialog
import com.dezdeqness.presentation.models.AnimeSearchFilter
import com.dezdeqness.ui.GridSpacingItemDecoration
import kotlinx.coroutines.launch

class AnimeListFragment :
    BaseFragment<FragmentAnimeListBinding>(),
    ActionListener, BackFragmentListener {

    private val searchView: SearchView by lazy {
        ItemSearchViewBinding.inflate(layoutInflater).root
    }

    private val adapter: AnimeListAdapter by lazy {
        AnimeListAdapter(
            actionListener = this,
            loadMoreCallback = {
                viewModel.onLoadMore()
            },
        )
    }

    private val eventConsumer: EventConsumer by lazy {
        EventConsumer(
            fragment = this,
        )
    }

    private val viewModel: AnimeViewModel by viewModels(factoryProducer = { viewModelFactory })

    override fun getFragmentBinding(layoutInflater: LayoutInflater) =
        FragmentAnimeListBinding.inflate(layoutInflater)

    override fun setupScreenComponent(component: AppComponent) =
        component
            .animeComponent()
            .create()
            .inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(AnimeSearchFilterBottomSheetDialog.TAG) { _, bundle ->
            val filtersList =
                bundle.getParcelableArrayList<AnimeSearchFilter>(AnimeSearchFilterBottomSheetDialog.RESULT)
                    .orEmpty()
            viewModel.applyFilter(filtersList)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRefreshLayout()
        setupRecyclerView()
        setupObservers()
        setupFab()
        setupMenu()
    }

    override fun onDestroy() {
        super.onDestroy()
        clearFragmentResultListener(AnimeSearchFilterBottomSheetDialog.TAG)
    }

    override fun onActionReceive(action: Action) {
        viewModel.onActionReceive(action)
    }

    private fun setupMenu() {
        binding.searchToolbar.apply {
            inflateMenu(R.menu.menu_search)
            title = null

            menu?.findItem(R.id.action_search)?.actionView = searchView
            searchView.apply {
                setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String): Boolean {
                        viewModel.onQueryChanged(query)
                        return true
                    }

                    override fun onQueryTextChange(newText: String): Boolean {
                        if (newText.isEmpty()) {
                            viewModel.onQueryEmpty()
                        }
                        return false
                    }

                })
                setOnCloseListener {
                    return@setOnCloseListener true
                }
            }
        }
    }

    override fun isBackNeed() = searchView.isIconified

    override fun onBackPressed() {
        binding.searchToolbar.menu?.findItem(R.id.action_search)?.collapseActionView()
    }

    private fun setupRefreshLayout() {
        binding.refresh.setOnRefreshListener {
            viewModel.onPullDownRefreshed()
        }
    }

    private fun setupFab() {
        binding.filterAction.setOnClickListener {
            if (parentFragmentManager.findFragmentByTag(AnimeSearchFilterBottomSheetDialog.TAG) == null) {
                viewModel.onFabClicked()
            }
        }
    }

    private fun setupRecyclerView() {
        binding.recycler.adapter = adapter
        // TODO: Dynamic columns
        binding.recycler.addItemDecoration(GridSpacingItemDecoration())
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.animeStateFlow.collect { state ->

                    val isLoadingStateShowing =
                        if (state.list.isEmpty() && state.isEmptyStateShowing.not()) {
                            state.isInitialLoadingIndicatorShowing
                        } else {
                            false
                        }

                    binding.recycler.setLoadingState(
                        isLoadingStateShowing = isLoadingStateShowing,
                    )

                    binding.recycler.setEmptyState(
                        isEmptyStateShowing = state.isEmptyStateShowing,
                    )

                    binding.refresh.isRefreshing = state.isPullDownRefreshing

                    adapter.submitList(state.list, state.hasNextPage) {
                        if (state.events.contains(ScrollToTop)) {
                            binding.recycler.scrollToPosition(0)
                            viewModel.onEventConsumed(ScrollToTop)
                        }
                    }

                    state.events.forEach { event ->
                        when (event) {
                            is NavigateToFilter -> {
                                val dialog =
                                    AnimeSearchFilterBottomSheetDialog.newInstance(event.filters)
                                dialog.show(
                                    parentFragmentManager,
                                    AnimeSearchFilterBottomSheetDialog.TAG
                                )
                            }

                            is ConsumableEvent -> {
                                eventConsumer.consume(event)
                            }

                            else -> {}
                        }

                        viewModel.onEventConsumed(event)

                    }
                }
            }
        }
    }

}
