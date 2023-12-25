package com.dezdeqness.presentation.features.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.dezdeqness.core.BaseFragment
import com.dezdeqness.databinding.FragmentCalendarBinding
import com.dezdeqness.di.AppComponent
import com.dezdeqness.presentation.action.Action
import com.dezdeqness.presentation.action.ActionListener
import com.dezdeqness.presentation.event.ConsumableEvent
import com.dezdeqness.presentation.event.EventConsumer
import com.dezdeqness.presentation.event.ScrollToTop
import kotlinx.coroutines.launch

class CalendarFragment : BaseFragment<FragmentCalendarBinding>(), ActionListener {

    private val viewModel: CalendarViewModel by viewModels(factoryProducer = { viewModelFactory })

    private val adapter: CalendarAdapter by lazy {
        CalendarAdapter(
            actionListener = this,
        )
    }

    private val eventConsumer: EventConsumer by lazy {
        EventConsumer(
            fragment = this,
        )
    }

    override fun getFragmentBinding(layoutInflater: LayoutInflater) =
        FragmentCalendarBinding.inflate(layoutInflater)

    override fun setupScreenComponent(component: AppComponent) =
        component
            .calendarComponent()
            .create()
            .inject(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRefreshLayout()
        setupRecyclerView()
        setupObservers()
        setupSearchView()
    }

    override fun onActionReceive(action: Action) {
        viewModel.onActionReceive(action)
    }

    private fun setupRecyclerView() {
        binding.recycler.adapter = adapter
    }

    private fun setupRefreshLayout() {
        binding.refresh.setOnRefreshListener {
            viewModel.onPullDownRefreshed()
        }
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

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.calendarStateFlow.collect { state ->
                    binding.refresh.isRefreshing = state.isPullDownRefreshing

                    val isLoadingStateShowing =
                        if (state.items.isEmpty() && state.isEmptyStateShowing.not()) {
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

                    binding.recycler.setErrorState(
                        isErrorStateShowing = state.isErrorStateShowing,
                    )

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
                    when (event) {
                        is ScrollToTop -> {
                            binding.recycler.scrollToPosition(0)
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
