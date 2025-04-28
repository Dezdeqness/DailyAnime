package com.dezdeqness.presentation.features.genericlistscreen

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.dezdeqness.core.BaseComposeFragment
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.presentation.action.Action
import com.dezdeqness.presentation.event.ConsumableEvent
import com.dezdeqness.presentation.event.Event
import com.dezdeqness.presentation.event.EventConsumer
import kotlinx.coroutines.launch

abstract class GenericListableFragment : BaseComposeFragment() {

    private var onBackPressedCallback: OnBackPressedCallback? = null

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

    abstract fun getRenderer(): GenericRenderer

    open fun onEvent(event: Event) = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBackPressedCallback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().popBackStack()
        }
    }

    @Composable
    override fun FragmentContent() {
        AppTheme {
            CompositionLocalProvider(LocalAdapterItemRenderer provides getRenderer()) {
                GenericListPage(
                    title = stringResource(getTitleRes()),
                    stateFlow = viewModel.genericListableStateFlow,
                    actions = object : GenericListableActions {
                        override fun onActionReceive(action: Action) {
                            viewModel.onActionReceive(action)
                        }

                        override fun onRetryClicked() {
                            viewModel.onRetryButtonClicked()
                        }

                        override fun onBackPressed() {
                            findNavController().popBackStack()
                        }
                    },
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
    }

    private fun setupObservers() {
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

}
