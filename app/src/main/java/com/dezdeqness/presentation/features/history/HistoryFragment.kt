package com.dezdeqness.presentation.features.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.dezdeqness.R
import com.dezdeqness.core.BaseFragment
import com.dezdeqness.databinding.FragmentHistoryBinding
import com.dezdeqness.di.AppComponent
import kotlinx.coroutines.launch

class HistoryFragment : BaseFragment<FragmentHistoryBinding>() {

    private val viewModel: HistoryViewModel by viewModels(factoryProducer = { viewModelFactory })

    private val adapter: HistoryListAdapter by lazy {
        HistoryListAdapter(
            loadMoreCallback = {
                viewModel.onLoadMore()
            },
        )
    }

    override fun setupScreenComponent(component: AppComponent) {
        component
            .historyComponent()
            .create()
            .inject(this)
    }

    override fun getFragmentBinding(layoutInflater: LayoutInflater) =
        FragmentHistoryBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRefreshLayout()
        setupRecyclerView()
        setupObservers()
        setupToolbar()
    }

    private fun setupToolbar() {
        with(binding.historyToolbar) {
            title = context.getString(R.string.history_toolbar_title)
            setNavigationIcon(R.drawable.ic_back)
            setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.historyStateFlow.collect { state ->

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

                    }
                }
            }
        }
    }

    private fun setupRefreshLayout() {
        binding.refresh.setOnRefreshListener {
            viewModel.onPullDownRefreshed()
        }
    }

    private fun setupRecyclerView() {
        binding.recycler.adapter = adapter
    }

}
