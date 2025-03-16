package com.dezdeqness.presentation.features.unauthorized

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.fragment.navArgs
import com.dezdeqness.core.BaseComposeFragment
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.data.analytics.AnalyticsManager
import com.dezdeqness.di.AppComponent
import com.dezdeqness.presentation.routing.ApplicationRouter

class UnauthorizedFragment : BaseComposeFragment() {

    private val args by navArgs<UnauthorizedFragmentArgs>()

    lateinit var analyticsManager: AnalyticsManager

    lateinit var applicationRouter: ApplicationRouter

    override fun setupScreenComponent(component: AppComponent) {
        applicationRouter = component.applicationRouter
        analyticsManager = component.analyticsManager
    }

    @Composable
    override fun FragmentContent() {
        AppTheme {
            UnauthorizedScreen(
                title = stringResource(args.titleResId),
                actions = object : UnauthorizedActions {
                    override fun onSignInClicked() {
                        analyticsManager.authTracked()
                        applicationRouter.navigateToLoginScreen(requireContext())
                    }

                    override fun onSignUpClicked() {
                        analyticsManager.authTracked(isLogin = false)
                        applicationRouter.navigateToSignUpScreen(requireContext())
                    }
                }
            )
        }

    }

}
