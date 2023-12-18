package com.dezdeqness.presentation.features.unauthorized.host

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
import com.dezdeqness.databinding.FragmentPersonalHostFragmentBinding
import com.dezdeqness.di.AppComponent
import com.dezdeqness.presentation.event.Event
import kotlinx.coroutines.launch

abstract class BaseUnauthorizedHostFragment : BaseFragment<FragmentPersonalHostFragmentBinding>() {

    private val viewModel: UnathorizedHostViewModel by viewModels(
        factoryProducer = {
            viewModelFactory
        }
    )

    abstract fun getNavigationGraph(): Int

    abstract fun getStartDestination(isAuthorized: Boolean): Int

    abstract fun consumeEvent(event: Event)

    open fun getPageArguments() = bundleOf()

    override fun setupScreenComponent(component: AppComponent) {
        component
            .unauthorizedHostComponent()
            .create()
            .inject(this)
    }

    override fun getFragmentBinding(layoutInflater: LayoutInflater) =
        FragmentPersonalHostFragmentBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
    }

    protected fun getNavController() =
        (childFragmentManager.findFragmentById(R.id.container) as NavHostFragment).navController

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.hostStateFlow.collect { state ->

                    val navController = getNavController()
                    val navGraph = navController.navInflater.inflate(getNavigationGraph())

                    val startDestination = getStartDestination(state.isAuthorized)

                    navGraph.setStartDestination(startDestination)
                    navController.setGraph(navGraph, getPageArguments())
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collect { event ->
                    consumeEvent(event)
                }
            }
        }

    }

}
