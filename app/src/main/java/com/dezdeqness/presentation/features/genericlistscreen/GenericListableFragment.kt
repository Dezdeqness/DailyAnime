package com.dezdeqness.presentation.features.genericlistscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.annotation.StringRes
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.dezdeqness.core.BaseFragment
import com.dezdeqness.core.DelegateAdapter
import com.dezdeqness.databinding.FragmentGenericListableBinding
import com.dezdeqness.presentation.action.Action
import com.dezdeqness.presentation.action.ActionListener
import com.dezdeqness.presentation.event.ConsumableEvent
import com.dezdeqness.presentation.event.Event
import com.dezdeqness.presentation.event.EventConsumer
import com.dezdeqness.presentation.models.AdapterItem
import kotlinx.coroutines.launch

abstract class GenericListableFragment :
    BaseFragment<FragmentGenericListableBinding>(), ActionListener {

    private var onBackPressedCallback: OnBackPressedCallback? = null

    private val adapter by lazy { delegateAdapter() }

    private val eventConsumer: EventConsumer by lazy {
        EventConsumer(
            fragment = this,
            context = this.requireContext(),
        )
    }

    private val viewModel: GenericListableViewModel by viewModels(
        factoryProducer = { viewModelFactory },
    )

    @StringRes
    abstract fun getTitleRes(): Int

    abstract fun delegateAdapter(): DelegateAdapter<AdapterItem>

    open fun onEvent(event: Event) = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBackPressedCallback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().popBackStack()
        }
    }

    override fun getFragmentBinding(layoutInflater: LayoutInflater) =
        FragmentGenericListableBinding.inflate(layoutInflater)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedCallback?.handleOnBackPressed()
        }

        setupToolbar()
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
                viewModel.genericListableStateFlow.collect { state ->
                    adapter.submitList(state.list)

                    binding.recycler.setEmptyState(
                        isEmptyStateShowing = state.isEmptyStateShowing,
                    )

                    binding.recycler.setErrorState(
                        isErrorStateShowing = state.isErrorStateShowing,
                    )

                    binding.refresh.isRefreshing = state.isPullDownRefreshing
                    setupLoadingState(state = state)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collect { event ->
                    val isConsumed = onEvent(event)
                    if (isConsumed) {
                        return@collect
                    }

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

    private fun setupLoadingState(state: GenericListableState) {
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

    private fun setupToolbar() {
        binding.toolbar.title = getString(getTitleRes())
    }

}
