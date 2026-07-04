package com.dezdeqness.presentation.features.stats

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.dezdeqness.ShikimoriApp
import com.dezdeqness.feature.stats.presentation.StatsPage
import com.dezdeqness.feature.stats.presentation.profile.ProfileStatsViewModel

@Composable
fun StatsStandalonePage(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    val context = LocalContext.current
    val statsComponent = remember {
        (context.applicationContext as ShikimoriApp).appComponent
            .statsComponent()
            .create()
    }

    val viewModel = viewModel<ProfileStatsViewModel>(factory = statsComponent.viewModelFactory())

    StatsPage(
        modifier = modifier,
        state = viewModel.statsStateFlow,
        onBackPressed = {
            navController.popBackStack()
        },
    )
}
