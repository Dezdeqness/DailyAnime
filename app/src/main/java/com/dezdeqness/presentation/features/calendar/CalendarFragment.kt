package com.dezdeqness.presentation.features.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.dezdeqness.R
import com.dezdeqness.core.BaseFragment
import com.dezdeqness.databinding.FragmentCalendarBinding
import com.dezdeqness.di.AppComponent
import com.dezdeqness.presentation.Event
import kotlinx.coroutines.launch

class CalendarFragment : BaseFragment<FragmentCalendarBinding>() {

    private val viewModel: CalendarViewModel by viewModels(factoryProducer = { viewModelFactory })

    private val adapter: CalendarAdapter by lazy {
        CalendarAdapter(
            onAnimeClickListener = { animeId ->
                findNavController().navigate(
                    R.id.animeDetailsFragment,
                    bundleOf("animeId" to animeId),
                )
            }
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

    private fun setupRecyclerView() {
        binding.calendar.adapter = adapter
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
                    adapter.submitList(state.items) {
                        if (state.events.contains(Event.ScrollToTop)) {
                            binding.calendar.scrollToPosition(0)
                            viewModel.onEventConsumed(Event.ScrollToTop)
                        }
                    }
                }
            }
        }
    }

}
