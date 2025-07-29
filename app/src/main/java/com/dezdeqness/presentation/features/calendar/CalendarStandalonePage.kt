package com.dezdeqness.presentation.features.calendar

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
fun CalendarStandalonePage(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {

    val context = LocalContext.current
    val homeComponent = remember {
        (context.applicationContext as ShikimoriApp).appComponent
            .calendarComponent()
            .create()
    }

    val viewModel = viewModel<CalendarViewModel>(factory = homeComponent.viewModelFactory())

    val analyticsManager = homeComponent.analyticsManager()

    CalendarPage(
        modifier = modifier,
        stateFlow = viewModel.calendarStateFlow,
        actions = object : CalendarActions {
            override fun onInitialLoad() {
                viewModel.onInitialLoad()
            }

            override fun onPullDownRefreshed() {
                viewModel.onPullDownRefreshed()
            }

            override fun onScrolled() {
                viewModel.onScrolled()
            }

            override fun onActionReceived(action: Action) {
                viewModel.onActionReceive(action)
            }

            override fun onQueryChanged(query: String) {
                viewModel.onQueryChanged(query)
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
