package com.dezdeqness.presentation.features.animesimilar

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
import com.dezdeqness.databinding.FragmentAnimeSimilarBinding
import com.dezdeqness.di.AppComponent
import com.dezdeqness.di.subcomponents.SimilarArgsModule
import com.dezdeqness.presentation.action.Action
import com.dezdeqness.presentation.action.ActionListener
import com.dezdeqness.presentation.event.ConsumableEvent
import com.dezdeqness.presentation.event.EventConsumer
import com.dezdeqness.presentation.features.animesimilar.recyclerview.AnimeSimilarAdapter
import kotlinx.coroutines.launch

class AnimeSimilarFragment : BaseFragment<FragmentAnimeSimilarBinding>(), ActionListener {

    private var onBackPressedCallback: OnBackPressedCallback? = null

    private val adapter by lazy {
        AnimeSimilarAdapter(
            actionListener = this,
        )
    }

    private val eventConsumer: EventConsumer by lazy {
        EventConsumer(
            fragment = this,
        )
    }

    private val viewModel: AnimeSimilarViewModel by viewModels(
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
            .animeSimilarComponent()
            .argsModule(SimilarArgsModule(requireArguments().getLong("animeId")))
            .build()
            .inject(this)
    }

    override fun getFragmentBinding(layoutInflater: LayoutInflater) =
        FragmentAnimeSimilarBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.similarToolbar.setNavigationOnClickListener {
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
                viewModel.similarStateFlow.collect { state ->
                    setupLoadingState(state = state)

                    binding.recycler.setEmptyState(
                        isEmptyStateShowing = state.isEmptyStateShowing,
                    )

                    binding.recycler.setErrorState(
                        isErrorStateShowing = state.isErrorStateShowing,
                    )

                    binding.refresh.isRefreshing = state.isPullDownRefreshing
                    adapter.submitList(state.list)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collect { event ->
                    when (event) {
                        is ConsumableEvent -> {
                            eventConsumer.consume(event)
                        }

                        else -> {}
                    }
                }
            }
        }
    }

    private fun setupLoadingState(state: AnimeSimilarState) {
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
