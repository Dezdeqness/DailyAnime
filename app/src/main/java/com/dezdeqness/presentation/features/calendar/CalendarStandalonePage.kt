package com.dezdeqness.presentation.features.calendar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.dezdeqness.ShikimoriApp
import com.dezdeqness.feature.calendar.presentation.CalendarActions
import com.dezdeqness.feature.calendar.presentation.CalendarPage
import com.dezdeqness.feature.calendar.presentation.CalendarViewModel
import com.dezdeqness.presentation.AnimeDetails

@Composable
fun CalendarStandalonePage(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    val context = LocalContext.current
    val calendarComponent = remember {
        (context.applicationContext as ShikimoriApp).appComponent
            .calendarComponent()
            .create()
    }

    val viewModel = viewModel<CalendarViewModel>(factory = calendarComponent.viewModelFactory())

    val analyticsManager = calendarComponent.analyticsManager()

    CalendarPage(
        modifier = modifier,
        stateFlow = viewModel.calendarStateFlow,
        pullRefreshFlow = viewModel.pullRefreshFlow,
        scrollNeedFlow = viewModel.scrollNeedFlow,
        actions = object : CalendarActions {
            override fun onPullDownRefreshed() {
                viewModel.onPullDownRefreshed()
            }

            override fun onScrolled() {
                viewModel.onScrolled()
            }

            override fun onAnimeClicked(animeId: Long, title: String) {
                analyticsManager.detailsTracked(
                    id = animeId.toString(),
                    title = title,
                )

                navController.navigate(AnimeDetails(animeId))
            }

            override fun onQueryChanged(query: String) {
                viewModel.onQueryChanged(query)
            }
        },
    )
}
