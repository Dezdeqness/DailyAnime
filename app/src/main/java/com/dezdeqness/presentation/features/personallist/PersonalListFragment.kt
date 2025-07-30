package com.dezdeqness.presentation.features.personallist

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityOptionsCompat
import androidx.navigation.NavHostController
import com.dezdeqness.core.utils.collectEvents
import com.dezdeqness.data.analytics.AnalyticsManager
import com.dezdeqness.presentation.Details
import com.dezdeqness.presentation.action.Action
import com.dezdeqness.presentation.event.AnimeDetails
import com.dezdeqness.presentation.event.ConsumableEvent
import com.dezdeqness.presentation.event.EventConsumer
import com.dezdeqness.presentation.event.NavigateToEditRate
import com.dezdeqness.presentation.features.userrate.UserRateActivity

@Composable
fun PersonalListStandalonePage(
    modifier: Modifier = Modifier,
    viewModel: PersonalListViewModel,
    analyticsManager: AnalyticsManager,
    navController: NavHostController,
) {
    val context = LocalContext.current

    val eventConsumer = remember {
        EventConsumer(
            context = context,
        )
    }

    val editRateResult = rememberLauncherForActivityResult(UserRateActivity.UserRate()) { userRate ->
        viewModel.onUserRateChanged(userRate)
    }

    PersonalListPage(
        modifier = modifier,
        stateFlow = viewModel.personalListStateFlow,
        actions = object : PersonalListActions {
            override fun onPullDownRefreshed() {
                viewModel.onPullDownRefreshed()
            }

            override fun onScrolled() {
                viewModel.onScrolled()
            }

            override fun onInitialLoad() {
                viewModel.onInitialLoad()
            }

            override fun onActionReceived(action: Action) {
                viewModel.onActionReceive(action)
            }

            override fun onQueryChanged(query: String) {
                viewModel.onQueryChanged(query)
            }

            override fun onOrderChanged(order: String) {
                viewModel.onSortChanged(order)
            }

            override fun onRibbonItemSelected(id: String) {
                viewModel.onRibbonItemSelected(id)
            }
        }
    )

    viewModel.events.collectEvents { event ->
        when (event) {
            is NavigateToEditRate -> {
                editRateResult.launch(
                    UserRateActivity.UserRateParams(
                        userRateId = event.rateId,
                        title = event.title,
                    ),
                    ActivityOptionsCompat.makeSceneTransitionAnimation(context as Activity),
                )
            }

            is AnimeDetails -> {
                analyticsManager.detailsTracked(
                    id = event.animeId.toString(),
                    title = event.title
                )

                navController.navigate(Details(event.animeId))
            }

            is ConsumableEvent -> {
                eventConsumer.consume(event)
            }

            else -> {}
        }
    }
}
