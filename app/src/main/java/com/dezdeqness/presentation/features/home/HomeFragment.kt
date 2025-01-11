package com.dezdeqness.presentation.features.home

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
import com.dezdeqness.di.AppComponent
import com.dezdeqness.presentation.action.Action
import com.dezdeqness.presentation.event.AnimeDetails
import com.dezdeqness.presentation.features.animelist.AnimeListFragmentDirections
import kotlinx.coroutines.launch

class HomeFragment : BaseComposeFragment() {

    private val viewModel: HomeViewModel by viewModels(factoryProducer = { viewModelFactory })

    override fun setupScreenComponent(component: AppComponent) {
        component
            .homeComponent()
            .create()
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
    }

    @Composable
    override fun FragmentContent() {
        AppTheme {
            HomePage(
                stateFlow = viewModel.homeStateFlow,
                actions = object : HomeActions {
                    override fun onInitialLoad() {
                        viewModel.onInitialLoad()
                    }

                    override fun onActionReceived(action: Action) {
                        viewModel.onActionReceive(action)
                    }
                },
            )
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collect { event ->
                    when (event) {
                        is AnimeDetails -> {
                            findNavController().navigate(
                                AnimeListFragmentDirections.navigateToAnimeDetails(event.animeId)
                            )
                        }

                        else -> {}
                    }
                }
            }
        }
    }
}
