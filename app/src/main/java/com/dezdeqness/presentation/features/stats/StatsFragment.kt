package com.dezdeqness.presentation.features.stats

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
import com.dezdeqness.databinding.FragmentStatsBinding
import com.dezdeqness.di.AppComponent
import kotlinx.coroutines.launch

class StatsFragment : BaseFragment<FragmentStatsBinding>() {

    private val adapter: StatsListAdapter by lazy {
        StatsListAdapter()
    }

    private val viewModel: StatsViewModel by viewModels(
        factoryProducer = {
            viewModelFactory
        }
    )

    override fun setupScreenComponent(component: AppComponent) {
        component
            .statsComponent()
            .create()
            .inject(this)
    }

    override fun getFragmentBinding(layoutInflater: LayoutInflater) =
        FragmentStatsBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupRecyclerView()
        setupObservers()
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.statsStateFlow.collect { state ->
                    adapter.submitList(state.items)
                }
            }
        }
    }

    private fun setupRecyclerView() {
        binding.recycler.adapter = adapter
    }

    private fun setupToolbar() {
        with(binding.statsToolbar) {
            title = context.getString(R.string.stats_toolbar_title)
            setNavigationIcon(R.drawable.ic_back)
            setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }
    }

}
