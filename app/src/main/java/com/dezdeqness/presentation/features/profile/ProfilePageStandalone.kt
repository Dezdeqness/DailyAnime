package com.dezdeqness.presentation.features.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.dezdeqness.ShikimoriApp
import com.dezdeqness.feature.profile.presentation.ProfileActions
import com.dezdeqness.feature.profile.presentation.ProfilePage
import com.dezdeqness.feature.profile.presentation.ProfileViewModel
import com.dezdeqness.presentation.Achievements
import com.dezdeqness.presentation.Favourites
import com.dezdeqness.presentation.History
import com.dezdeqness.presentation.Settings
import com.dezdeqness.presentation.Stats

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

    val analyticsManager = profileComponent.analyticsManager()
    val applicationRouter = profileComponent.applicationRouter()

    ProfilePage(
        modifier = modifier,
        stateFlow = viewModel.profileStateFlow,
        actions = object : ProfileActions {
            override fun onSettingIconClicked() {
                analyticsManager.settingsTracked()
                navController.navigate(Settings)
            }

            override fun onStatsIconClicked() {
                navController.navigate(Stats)
            }

            override fun onHistoryIconClicked() {
                navController.navigate(History)
            }

            override fun onAchievementsClicked(userId: Long) {
                navController.navigate(Achievements(userId = userId))
            }

            override fun onFavouriteClicked(userId: Long) {
                navController.navigate(Favourites(userId = userId))
            }

            override fun onLoginClicked() {
                analyticsManager.authTracked()
                applicationRouter.navigateToLoginScreen(context)
            }

            override fun onRegistrationClicked() {
                analyticsManager.authTracked(isLogin = false)
                applicationRouter.navigateToSignUpScreen(context)
            }

            override fun onLogoutClicked() {
                viewModel.onLogoutClicked()
            }
        },
    )
}
