package com.dezdeqness.presentation.features.animestats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.dezdeqness.R
import com.dezdeqness.core.BaseFragment
import com.dezdeqness.databinding.FragmentAnimeStatsBinding
import com.dezdeqness.di.AppComponent
import com.dezdeqness.di.subcomponents.AnimeStatsArgsModule
import com.dezdeqness.presentation.features.stats.StatsListAdapter
import kotlinx.coroutines.launch

class AnimeStatsFragment : BaseFragment<FragmentAnimeStatsBinding>() {

    private val args by navArgs<AnimeStatsFragmentArgs>()

    private val viewModel: AnimeStatsViewModel by viewModels(
        factoryProducer = { viewModelFactory },
    )

    private val adapter: StatsListAdapter by lazy {
        StatsListAdapter()
    }

    override fun setupScreenComponent(component: AppComponent) {
        component
            .animeStatsComponent()
            .argsModule(
                AnimeStatsArgsModule(
                    AnimeStatsArguments(
                        scoresArgument = args.animeScores.toList(),
                        statusesArgument = args.animeStatuses.toList(),
                    )
                )
            )
            .build()
            .inject(this)
    }

    override fun getFragmentBinding(layoutInflater: LayoutInflater) =
        FragmentAnimeStatsBinding.inflate(getLayoutInflater())

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
