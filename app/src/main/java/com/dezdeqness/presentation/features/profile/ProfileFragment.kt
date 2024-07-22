package com.dezdeqness.presentation.features.profile

import android.os.Bundle
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.dezdeqness.R
import com.dezdeqness.core.BaseComposeFragment
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.di.AppComponent
import com.dezdeqness.presentation.event.NavigateToHistory
import com.dezdeqness.presentation.event.NavigateToLoginPage
import com.dezdeqness.presentation.event.NavigateToSettings
import com.dezdeqness.presentation.event.NavigateToSignUp
import com.dezdeqness.presentation.event.NavigateToStats
import com.dezdeqness.presentation.features.authorization.AuthorizationActivity
import kotlinx.coroutines.launch

class ProfileFragment : BaseComposeFragment() {

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
            val state by viewModel.profileStateFlow.collectAsState()

            ProfileScreen(
                state = state,
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
                                .navigate(R.id.history)
                        }

                        NavigateToSettings -> {
                            this@ProfileFragment
                                .findNavController()
                                .navigate(R.id.settings)
                        }

                        NavigateToStats -> {
                            this@ProfileFragment
                                .findNavController()
                                .navigate(R.id.statistics)
                        }

                        NavigateToLoginPage -> {
                            AuthorizationActivity.startLoginFlow(requireContext())
                        }

                        NavigateToSignUp -> {
                            AuthorizationActivity.startSignUpFlow(requireContext())
                        }

                        else -> Unit
                    }
                }
            }
        }
    }
}
