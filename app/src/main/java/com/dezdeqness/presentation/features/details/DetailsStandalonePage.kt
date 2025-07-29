package com.dezdeqness.presentation.features.details

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.dezdeqness.ShikimoriApp
import com.dezdeqness.core.utils.collectEvents
import com.dezdeqness.di.subcomponents.ArgsModule
import com.dezdeqness.presentation.Chronology
import com.dezdeqness.presentation.Details
import com.dezdeqness.presentation.DetailsStats
import com.dezdeqness.presentation.Similar
import com.dezdeqness.presentation.action.Action
import com.dezdeqness.presentation.event.AnimeDetails
import com.dezdeqness.presentation.event.ConsumableEvent
import com.dezdeqness.presentation.event.EventConsumer
import com.dezdeqness.presentation.event.NavigateToAnimeStats
import com.dezdeqness.presentation.event.NavigateToCharacterDetails
import com.dezdeqness.presentation.event.NavigateToChronology
import com.dezdeqness.presentation.event.NavigateToEditRate
import com.dezdeqness.presentation.event.NavigateToScreenshotViewer
import com.dezdeqness.presentation.event.NavigateToSimilar
import com.dezdeqness.presentation.features.userrate.UserRateActivity

@Composable
fun DetailsStandalonePage(
    modifier: Modifier = Modifier,
    target: Target,
    navController: NavHostController,
) {

    val context = LocalContext.current
    val detailsComponent = remember {
        (context.applicationContext as ShikimoriApp).appComponent
            .animeDetailsComponent()
            .argsModule(ArgsModule(target))
            .build()
    }

    val viewModel = viewModel<AnimeDetailsViewModel>(factory = detailsComponent.viewModelFactory())

    val applicationRouter = detailsComponent.applicationRouter()
    val analyticsManager = detailsComponent.analyticsManager()

    val eventConsumer = remember {
        EventConsumer(context = context)
    }

    val editRateResult =
        rememberLauncherForActivityResult(UserRateActivity.UserRate()) { userRate ->
            viewModel.onUserRateChanged(userRate)
        }

    DetailsPage(
        modifier = modifier,
        stateFlow = viewModel.animeDetailsStateFlow,
        actions = object : DetailsActions {
            override fun onBackPressed() {
                navController.popBackStack()
            }

            override fun onSharePressed() {
                viewModel.onShareButtonClicked()
            }

            override fun onFabClicked() {
                viewModel.onEditRateClicked()
            }

            override fun onActionReceive(action: Action) {
                viewModel.onActionReceive(action)
            }

            override fun onRetryClicked() {
                viewModel.onRetryButtonClicked()
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

            is NavigateToAnimeStats -> {
                navController.navigate(
                    DetailsStats(
                        scoreString = serializeListToString(event.scoreList),
                        statusesString = serializeListToString(event.statusesList),
                    )
                )
            }

            is NavigateToSimilar -> {
                navController.navigate(Similar(event.animeId))
            }

            is NavigateToChronology -> {
                navController.navigate(Chronology(event.animeId))
            }

            is NavigateToScreenshotViewer -> {
                applicationRouter.navigateToScreenshotViewerScreen(
                    context = context,
                    screenshots = event.screenshots,
                    index = event.currentIndex,
                )
            }

            is AnimeDetails -> {
                analyticsManager.detailsTracked(id = event.animeId.toString(), title = event.title)

                navController.navigate(Details(event.animeId))
            }

            is NavigateToCharacterDetails -> {
                navController.navigate(Details(event.characterId, isAnime = false))
            }

            is ConsumableEvent -> {
                eventConsumer.consume(event)
            }

            else -> {}
        }

    }
}
