package com.dezdeqness.presentation.features.unauthorized

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.fragment.navArgs
import com.dezdeqness.core.BaseComposeFragment
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.di.AppComponent
import com.dezdeqness.presentation.routing.ApplicationRouter

class UnauthorizedFragment : BaseComposeFragment() {

    private val args by navArgs<UnauthorizedFragmentArgs>()

    lateinit var applicationRouter: ApplicationRouter

    override fun setupScreenComponent(component: AppComponent) {
        applicationRouter = component.applicationRouter
    }

    @Composable
    override fun FragmentContent() {
        AppTheme {
            UnauthorizedScreen(
                title = stringResource(requireArguments().getInt("titleResId")),
                actions = object : UnauthorizedActions {
                    override fun onSignInClicked() {
                        applicationRouter.navigateToLoginScreen(requireContext())
                    }

                    override fun onSignUpClicked() {
                        applicationRouter.navigateToSignUpScreen(requireContext())
                    }
                }
            )
        }

    }

}
