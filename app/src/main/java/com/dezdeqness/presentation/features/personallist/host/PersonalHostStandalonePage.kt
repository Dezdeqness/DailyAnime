package com.dezdeqness.presentation.features.personallist.host

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.dezdeqness.R
import com.dezdeqness.ShikimoriApp
import com.dezdeqness.core.AuthorizedUiState
import com.dezdeqness.presentation.features.personallist.PersonalListStandalonePage
import com.dezdeqness.presentation.features.personallist.PersonalListViewModel
import com.dezdeqness.presentation.features.unauthorized.UnauthorizedActions
import com.dezdeqness.presentation.features.unauthorized.UnauthorizedScreen
import com.dezdeqness.presentation.features.unauthorized.host.PersonalListHostViewModel

@Composable
fun PersonalHostStandalonePage(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    val context = LocalContext.current
    val personalListComponent = remember {
        (context.applicationContext as ShikimoriApp).appComponent
            .personalListComponent()
            .create()
    }

    val analyticsManager = personalListComponent.analyticsManager()
    val applicationRouter = personalListComponent.applicationRouter()

    val hostViewModel =
        viewModel<PersonalListHostViewModel>(factory = personalListComponent.viewModelFactory())

    val viewModel =
        viewModel<PersonalListViewModel>(factory = personalListComponent.viewModelFactory())

    val state by hostViewModel.hostStateFlow.collectAsStateWithLifecycle()

    when (state.authorizedState) {
        AuthorizedUiState.Authorized -> {
            PersonalListStandalonePage(
                modifier = modifier,
                viewModel = viewModel,
                navController = navController,
                analyticsManager = analyticsManager,
            )
        }

        AuthorizedUiState.Unauthorized -> {
            UnauthorizedScreen(
                modifier = modifier,
                title = stringResource(R.string.unauthorized_title_personal_list),
                actions = object : UnauthorizedActions {
                    override fun onSignInClicked() {
                        analyticsManager.authTracked()
                        applicationRouter.navigateToLoginScreen(context)
                    }

                    override fun onSignUpClicked() {
                        analyticsManager.authTracked(isLogin = false)
                        applicationRouter.navigateToSignUpScreen(context)
                    }
                }
            )
        }

        else -> {}
    }
}
