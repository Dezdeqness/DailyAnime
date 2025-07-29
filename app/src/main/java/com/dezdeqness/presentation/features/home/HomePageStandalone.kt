package com.dezdeqness.presentation.features.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.dezdeqness.ShikimoriApp
import com.dezdeqness.core.utils.collectEvents
import com.dezdeqness.presentation.Details
import com.dezdeqness.presentation.action.Action
import com.dezdeqness.presentation.event.AnimeDetails

@Composable
fun HomePageStandalone(
    modifier: Modifier = Modifier,
    navController: NavHostController,
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

            override fun onActionReceived(action: Action) {
                viewModel.onActionReceive(action)
            }
        },
    )


    viewModel.events.collectEvents { event ->
        when (event) {
            is AnimeDetails -> {
                analyticsManager.detailsTracked(
                    id = event.animeId.toString(),
                    title = event.title
                )

                navController.navigate(Details(event.animeId))
            }

            else -> {}
        }
    }
}
