package com.dezdeqness.presentation.features.profile

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
import com.dezdeqness.presentation.event.NavigateToHistory
import com.dezdeqness.presentation.event.NavigateToLoginPage
import com.dezdeqness.presentation.event.NavigateToSettings
import com.dezdeqness.presentation.event.NavigateToSignUp
import com.dezdeqness.presentation.event.NavigateToStats
import com.dezdeqness.presentation.routing.ApplicationRouter
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileFragment : BaseComposeFragment() {

    @Inject
    lateinit var applicationRouter: ApplicationRouter

    private val viewModel: ProfileViewModel by viewModels(
        factoryProducer = {
            viewModelFactory
        }
    )

    override fun setupScreenComponent(component: AppComponent) =
        component
            .profileComponent()
            .create()
            .inject(this)

    @Composable
    override fun FragmentContent() {
        AppTheme {
            ProfileScreen(
                stateFlow = viewModel.profileStateFlow,
                actions = object : ProfileActions {
                    override fun onSettingIconClicked() =
                        viewModel.onEventReceive(NavigateToSettings)

                    override fun onStatsIconClicked() =
                        viewModel.onEventReceive(NavigateToStats)

                    override fun onHistoryIconClicked() =
                        viewModel.onEventReceive(NavigateToHistory)

                    override fun onLoginCLicked() = viewModel.onEventReceive(NavigateToLoginPage)

                    override fun onLogoutClicked() {
                        viewModel.onLogoutClicked()
                    }

                    override fun onRegistrationClicked() =
                        viewModel.onEventReceive(NavigateToSignUp)
                })
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collect {
                    when (it) {
                        NavigateToHistory -> {
                            this@ProfileFragment
                                .findNavController()
                                .navigate(ProfileFragmentDirections.navigateToHistory())
                        }

                        NavigateToSettings -> {
                            this@ProfileFragment
                                .findNavController()
                                .navigate(ProfileFragmentDirections.navigateToSettings())
                        }

                        NavigateToStats -> {
                            this@ProfileFragment
                                .findNavController()
                                .navigate(ProfileFragmentDirections.navigateToStatistics())
                        }

                        NavigateToLoginPage -> {
                            applicationRouter.navigateToLoginScreen(requireContext())
                        }

                        NavigateToSignUp -> {
                            applicationRouter.navigateToSignUpScreen(requireContext())
                        }

                        else -> Unit
                    }
                }
            }
        }
    }
}
