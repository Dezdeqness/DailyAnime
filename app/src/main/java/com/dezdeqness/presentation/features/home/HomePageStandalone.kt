package com.dezdeqness.presentation.features.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.dezdeqness.ShikimoriApp
import com.dezdeqness.feature.home.presentation.HomeActions
import com.dezdeqness.feature.home.presentation.HomePage
import com.dezdeqness.feature.home.presentation.HomeViewModel
import com.dezdeqness.presentation.AnimeDetails
import com.dezdeqness.presentation.BottomBarNav
import com.dezdeqness.presentation.History

@Composable
fun HomePageStandalone(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    rootController: NavHostController,
) {
    val context = LocalContext.current
    val homeComponent = remember {
        (context.applicationContext as ShikimoriApp).appComponent
            .homeComponent()
            .create()
    }

    val viewModel = viewModel<HomeViewModel>(factory = homeComponent.viewModelFactory())

    val analyticsManager = homeComponent.analyticsManager()

    HomePage(
        modifier = modifier,
        stateFlow = viewModel.homeStateFlow,
        actions = object : HomeActions {
            override fun onInitialLoad() {
                viewModel.onInitialLoad()
            }

            override fun onAnimeClicked(animeId: Long, title: String) {
                analyticsManager.detailsTracked(
                    id = animeId.toString(),
                    title = title,
                )

                rootController.navigate(AnimeDetails(animeId))
            }

            override fun onCalendarHeaderClicked() {
                navController.navigate(BottomBarNav.Calendar) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }

            override fun onHistoryHeaderClicked() {
                rootController.navigate(History)
            }
        },
    )
}
