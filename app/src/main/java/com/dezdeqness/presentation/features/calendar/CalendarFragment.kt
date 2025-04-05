package com.dezdeqness.presentation.features.calendar

import android.os.Bundle
import android.view.View
import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.dezdeqness.core.BaseComposeFragment
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.data.analytics.AnalyticsManager
import com.dezdeqness.di.AppComponent
import com.dezdeqness.presentation.action.Action
import com.dezdeqness.presentation.event.AnimeDetails
import com.dezdeqness.presentation.event.ConsumableEvent
import com.dezdeqness.presentation.event.EventConsumer
import kotlinx.coroutines.launch
import javax.inject.Inject

class CalendarFragment : BaseComposeFragment() {

    @Inject
    lateinit var analyticsManager: AnalyticsManager

    private val viewModel: CalendarViewModel by viewModels(factoryProducer = { viewModelFactory })

    private val eventConsumer: EventConsumer by lazy {
        EventConsumer(
            fragment = this,
            context = this.requireContext(),
        )
    }

    override fun setupScreenComponent(component: AppComponent) =
        component
            .calendarComponent()
            .create()
            .inject(this)

    @Composable
    override fun FragmentContent() {
        AppTheme {
            CalendarPage(
                stateFlow = viewModel.calendarStateFlow,
                actions = object : CalendarActions {
                    override fun onInitialLoad() {
                        viewModel.onInitialLoad()
                    }

                    override fun onPullDownRefreshed() {
                        viewModel.onPullDownRefreshed()
                    }

                    override fun onScrolled() {
                        viewModel.onScrolled()
                    }

                    override fun onActionReceived(action: Action) {
                        viewModel.onActionReceive(action)
                    }

                    override fun onQueryChanged(query: String) {
                        viewModel.onQueryChanged(query)
                    }

                },
            )
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
                    when (event) {
                        is AnimeDetails -> {
                            analyticsManager.detailsTracked(
                                id = event.animeId.toString(),
                                title = event.title
                            )
                            findNavController().navigate(
                                CalendarFragmentDirections.navigateToAnimeDetails(event.animeId)
                            )
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
