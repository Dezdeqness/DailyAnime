package com.dezdeqness.presentation.features.animechronology

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.dezdeqness.core.BaseFragment
import com.dezdeqness.databinding.FragmentAnimeChronologyBinding
import com.dezdeqness.di.AppComponent
import com.dezdeqness.di.subcomponents.ChronologyArgsModule
import com.dezdeqness.presentation.action.Action
import com.dezdeqness.presentation.action.ActionListener
import com.dezdeqness.presentation.event.ConsumableEvent
import com.dezdeqness.presentation.event.EventConsumer
import com.dezdeqness.presentation.features.animechronology.recyclerview.AnimeChronologyAdapter
import com.dezdeqness.presentation.features.animesimilar.recyclerview.AnimeSimilarAdapter
import kotlinx.coroutines.launch

class AnimeChronologyFragment : BaseFragment<FragmentAnimeChronologyBinding>(), ActionListener {

    private var onBackPressedCallback: OnBackPressedCallback? = null

    private val adapter by lazy {
        AnimeChronologyAdapter(
            actionListener = this,
        )
    }

    private val eventConsumer: EventConsumer by lazy {
        EventConsumer(
            fragment = this,
        )
    }

    private val viewModel: AnimeChronologyViewModel by viewModels(
        factoryProducer = { viewModelFactory },
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBackPressedCallback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().popBackStack()
        }
    }

    override fun setupScreenComponent(component: AppComponent) {
        component
            .animeChronologyComponent()
            .argsModule(ChronologyArgsModule(requireArguments().getLong("animeId")))
            .build()
            .inject(this)
    }

    override fun getFragmentBinding(layoutInflater: LayoutInflater) =
        FragmentAnimeChronologyBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.chronologyToolbar.setNavigationOnClickListener {
            onBackPressedCallback?.handleOnBackPressed()
        }

        setupRefreshLayout()
        setupRecyclerView()
        setupObservers()
    }

    override fun onActionReceive(action: Action) {
        viewModel.onActionReceive(action)
    }

    private fun setupRecyclerView() {
        binding.recycler.adapter = adapter
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.chronologyStateFlow.collect { state ->
                    setupLoadingState(state = state)

                    binding.recycler.setEmptyState(
                        isEmptyStateShowing = state.isEmptyStateShowing,
                    )
                    binding.refresh.isRefreshing = state.isPullDownRefreshing
                    adapter.submitList(state.list)

                    state.events.forEach { event ->
                        when (event) {
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

    private fun setupLoadingState(state: AnimeChronologyState) {
        val isLoadingStateShowing =
            if (state.list.isEmpty() && state.isEmptyStateShowing.not()) {
                state.isInitialLoadingIndicatorShowing
            } else {
                false
            }

        binding.recycler.setLoadingState(
            isLoadingStateShowing = isLoadingStateShowing,
        )
    }

    private fun setupRefreshLayout() {
        binding.refresh.setOnRefreshListener {
            viewModel.onPullDownRefreshed()
        }
    }

}
