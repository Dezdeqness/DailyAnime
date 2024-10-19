package com.dezdeqness.presentation.features.personallist

import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.SearchView
import androidx.core.view.forEach
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.dezdeqness.R
import com.dezdeqness.core.BaseFragment
import com.dezdeqness.databinding.FragmentPersonalListBinding
import com.dezdeqness.di.AppComponent
import com.dezdeqness.presentation.action.Action
import com.dezdeqness.presentation.action.ActionListener
import com.dezdeqness.presentation.event.AnimeDetails
import com.dezdeqness.presentation.event.ConsumableEvent
import com.dezdeqness.presentation.event.Event
import com.dezdeqness.presentation.event.EventConsumer
import com.dezdeqness.presentation.event.NavigateToEditRate
import com.dezdeqness.presentation.event.OpenMenuPopupFilter
import com.dezdeqness.presentation.event.ScrollToTop
import com.dezdeqness.presentation.features.userrate.UserRateActivity
import kotlinx.coroutines.launch


class PersonalListFragment : BaseFragment<FragmentPersonalListBinding>(), ActionListener {

    private val adapter: PersonalListAdapter by lazy {
        PersonalListAdapter(
            actionListener = this,
            onEditRateClicked = { editRateId ->
                viewModel.onEditRateClicked(editRateId)
            }
        )
    }

    private val eventConsumer: EventConsumer by lazy {
        EventConsumer(
            fragment = this,
            context = this.requireContext(),
        )
    }

    private val editRateResult = registerForActivityResult(UserRateActivity.UserRate()) { userRate ->
        viewModel.onUserRateChanged(userRate)
    }

    private val viewModel: PersonalListViewModel by viewModels(factoryProducer = { viewModelFactory })

    private var sortPopUpMenu: PopupMenu? = null

    override fun getFragmentBinding(layoutInflater: LayoutInflater) =
        FragmentPersonalListBinding.inflate(layoutInflater)

    override fun setupScreenComponent(component: AppComponent) =
        component
            .personalListComponent()
            .create()
            .inject(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRefreshLayout()
        setupRecyclerView()
        setupSearchView()
        setupObservers()
        setupMenu()
        setupSortMenu()
    }

    override fun onActionReceive(action: Action) {
        viewModel.onActionReceive(action)
    }

    private fun setupSearchView() {
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.onQueryChanged(newText)
                return true
            }

        })
    }

    private fun setupRefreshLayout() {
        binding.refresh.setOnRefreshListener {
            viewModel.onPullDownRefreshed()
        }
    }

    private fun setupMenu() {
        binding.toolbar.apply {
            inflateMenu(R.menu.menu_personal_list)
            setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_filter -> {
                        viewModel.onFilterButtonClicked()
                    }
                }
                true
            }
            menu?.findItem(R.id.action_filter)?.isVisible = false
        }
    }

    private fun setupSortMenu() {
        val con = ContextThemeWrapper(requireContext(), R.style.SortPopUpMenu)
        sortPopUpMenu = PopupMenu(con, binding.toolbar, Gravity.END).apply {
            menuInflater.inflate(R.menu.menu_popup_filter, menu)
            setOnMenuItemClickListener { menuItem ->
                viewModel.onSortChanged(menuItem.titleCondensed.toString())
                true
            }
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.personalListStateFlow.collect { state ->
                    binding.refresh.isRefreshing = state.isPullDownRefreshing

                    setupShareButton(state = state)

                    setupLoadingState(state = state)
                    binding.recycler.setEmptyState(
                        isEmptyStateShowing = state.isEmptyStateShowing,
                    )

                    setupRibbon(state = state)
                    adapter.submitList(state.items) {
                        if (state.isScrollNeed) {
                            viewModel.onScrollNeed()
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collect { event ->
                    onEvent(event)
                }
            }
        }
    }

    private fun setupRibbon(state: PersonalListState) {
        if (state.ribbon.isNotEmpty()) {
            binding.ribbon.populate(
                list = state.ribbon,
                listener = { id ->
                    viewModel.onRibbonItemSelected(id = id)
                }
            )
        }
    }

    private fun onEvent(event: Event) {
        when (event) {
            is ScrollToTop -> {
                binding.recycler.scrollToPosition(0)
            }

            is NavigateToEditRate -> {
                editRateResult.launch(
                    UserRateActivity.UserRateParams(
                        userRateId = event.rateId,
                        title = event.title,
                        overallEpisodes = event.overallEpisodes,
                    )
                )
            }

            is OpenMenuPopupFilter -> {
                openPopupMenu(event.sort)
            }

            is AnimeDetails -> {
                findNavController().navigate(
                        PersonalListFragmentDirections.navigateToAnimeDetails(event.animeId)
                    )
            }

            is ConsumableEvent -> {
                eventConsumer.consume(event)
            }

            else -> {}
        }
    }

    private fun setupShareButton(state: PersonalListState) {
        if (state.items.isNotEmpty()) {
            binding.toolbar.menu?.findItem(R.id.action_filter)?.let { menuItem ->
                if (menuItem.isVisible.not()) {
                    menuItem.isVisible = true
                }
            }
        }
    }

    private fun setupLoadingState(state: PersonalListState) {
        val isLoadingStateShowing =
            if (state.items.isEmpty() && state.isEmptyStateShowing.not()) {
                state.isInitialLoadingIndicatorShowing
            } else {
                false
            }

        binding.recycler.setLoadingState(
            isLoadingStateShowing = isLoadingStateShowing,
        )
    }

    private fun openPopupMenu(sort: String) {
        sortPopUpMenu?.let {
            it.menu.forEach {
                if (it.titleCondensed == sort) {
                    it.isChecked = true
                    return@forEach
                }
            }
            it.show()
        }
    }

    private fun setupRecyclerView() {
        binding.recycler.adapter = adapter
    }

}
