package com.dezdeqness.presentation.features.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.dezdeqness.ShikimoriApp
import com.dezdeqness.core.utils.collectEvents
import com.dezdeqness.presentation.Achievements
import com.dezdeqness.presentation.History
import com.dezdeqness.presentation.Settings
import com.dezdeqness.presentation.Stats
import com.dezdeqness.presentation.event.NavigateToAchievements
import com.dezdeqness.presentation.event.NavigateToHistory
import com.dezdeqness.presentation.event.NavigateToLoginPage
import com.dezdeqness.presentation.event.NavigateToSettings
import com.dezdeqness.presentation.event.NavigateToSignUp
import com.dezdeqness.presentation.event.NavigateToStats

@Composable
fun ProfilePageStandalone(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    val context = LocalContext.current
    val profileComponent = remember {
        (context.applicationContext as ShikimoriApp).appComponent
            .profileComponent()
            .create()
    }

    val viewModel = viewModel<ProfileViewModel>(factory = profileComponent.viewModelFactory())

    ProfileScreen(
        modifier = modifier,
        stateFlow = viewModel.profileStateFlow,
        actions = object : ProfileActions {
            override fun onSettingIconClicked() =
                viewModel.onEventReceive(NavigateToSettings)

            override fun onStatsIconClicked() =
                viewModel.onEventReceive(NavigateToStats)

            override fun onHistoryIconClicked() =
                viewModel.onEventReceive(NavigateToHistory)

            override fun onAchievementsClicked(userId: Long) {
                viewModel.onEventReceive(NavigateToAchievements(userId))
            }

            override fun onLoginCLicked() = viewModel.onEventReceive(NavigateToLoginPage)

            override fun onLogoutClicked() {
                viewModel.onLogoutClicked()
            }

            override fun onRegistrationClicked() =
                viewModel.onEventReceive(NavigateToSignUp)
        },
    )

    viewModel.events.collectEvents { event ->
        when (event) {
            NavigateToHistory -> {
                navController.navigate(History)
            }

            NavigateToSettings -> {
                profileComponent.analyticsManager().settingsTracked()
                navController.navigate(Settings)
            }

            NavigateToStats -> {
                navController.navigate(Stats)
            }

            NavigateToLoginPage -> {
                profileComponent.analyticsManager().authTracked()
                profileComponent.applicationRouter().navigateToLoginScreen(context)
            }

            NavigateToSignUp -> {
                profileComponent.analyticsManager().authTracked(isLogin = false)
                profileComponent.applicationRouter().navigateToSignUpScreen(context)
            }

            is NavigateToAchievements -> {
                navController.navigate(Achievements(userId = event.usedId))
            }

            else -> Unit
        }
    }
}
