package com.dezdeqness.presentation.features.unauthorized

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.dezdeqness.core.BaseComposeFragment
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.di.AppComponent
import com.dezdeqness.presentation.features.authorization.AuthorizationActivity

class UnauthorizedFragment : BaseComposeFragment() {

    override fun setupScreenComponent(component: AppComponent) = Unit

    @Composable
    override fun FragmentContent() {
        AppTheme {
            UnauthorizedScreen(
                title = stringResource(requireArguments().getInt("titleResId")),
                actions = object : UnauthorizedActions {
                    override fun onSignInClicked() {
                        startActivity(AuthorizationActivity.loginIntent(requireContext()))
                    }

                    override fun onSignUpClicked() {
                        startActivity(AuthorizationActivity.signUpIntent(requireContext()))
                    }
                }
            )
        }

    }

}
