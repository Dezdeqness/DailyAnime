package com.dezdeqness.presentation.features.personallist.host

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import com.dezdeqness.R
import com.dezdeqness.core.AuthorizedUiState
import com.dezdeqness.core.BaseFragment
import com.dezdeqness.databinding.FragmentPersonalHostFragmentBinding
import com.dezdeqness.di.AppComponent
import com.dezdeqness.presentation.features.unauthorized.host.PersonalListHostViewModel
import kotlinx.coroutines.launch

class PersonalHostFragment :  BaseFragment<FragmentPersonalHostFragmentBinding>() {

    private val viewModel: PersonalListHostViewModel by viewModels(
        factoryProducer = {
            viewModelFactory
        }
    )

    override fun getFragmentBinding(layoutInflater: LayoutInflater) =
        FragmentPersonalHostFragmentBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
    }

    override fun setupScreenComponent(component: AppComponent) {
        component
            .unauthorizedHostComponent()
            .create()
            .inject(this)
    }

    private fun getStartDestination(isAuthorized: Boolean) =
        if (isAuthorized) {
            R.id.personalList
        } else {
            R.id.personalUnauthorized
        }

    private fun getNavController() =
        (childFragmentManager.findFragmentById(R.id.container) as NavHostFragment).navController

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.hostStateFlow.collect { state ->
                    if (state.authorizedState == AuthorizedUiState.Pending) return@collect

                    val navController = getNavController()
                    val navGraph = navController.navInflater.inflate(R.navigation.personal_list_nav_graph)

                    val isAuthorized = state.authorizedState == AuthorizedUiState.Authorized

                    val startDestination = getStartDestination(isAuthorized)

                    navGraph.setStartDestination(startDestination)
                    navController.setGraph(navGraph, bundleOf())
                }
            }
        }

    }

}
