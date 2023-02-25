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
import com.dezdeqness.core.BaseFragment
import com.dezdeqness.databinding.FragmentPersonalListHostFragmentBinding
import com.dezdeqness.di.AppComponent
import com.dezdeqness.presentation.event.NavigateToPersonalList
import com.dezdeqness.presentation.event.NavigateToUnauthorized
import kotlinx.coroutines.launch

class PersonalListHostFragment : BaseFragment<FragmentPersonalListHostFragmentBinding>() {

    private val viewModel: PersonalListHostViewModel by viewModels(
        factoryProducer = {
            viewModelFactory
        }
    )

    override fun setupScreenComponent(component: AppComponent) {
        component
            .personalListHostComponent()
            .create()
            .inject(this)
    }

    override fun getFragmentBinding(layoutInflater: LayoutInflater) =
        FragmentPersonalListHostFragmentBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.hostStateFlow.collect { state ->
                    val navHostFragment =
                        childFragmentManager.findFragmentById(R.id.container) as NavHostFragment
                    val navController = navHostFragment.navController
                    val navGraph =
                        navController.navInflater.inflate(R.navigation.personal_list_nav_graph)

                    val startDestination = if (state.isAuthorized) {
                        R.id.personalList
                    } else {
                        R.id.unauthorized
                    }
                    navGraph.setStartDestination(startDestination)
                    navController.setGraph(navGraph, bundleOf())

                    state.events.forEach { event ->
                        when (event) {
                            NavigateToUnauthorized -> {
                                navGraph.setStartDestination(R.id.unauthorized)
                                navController.setGraph(navGraph, bundleOf())
                            }

                            NavigateToPersonalList -> {
                                navGraph.setStartDestination(R.id.personalList)
                                navController.setGraph(navGraph, bundleOf())
                            }

                            else -> {}
                        }
                        viewModel.onEventConsumed(event)
                    }
                }
            }
        }
    }

}
