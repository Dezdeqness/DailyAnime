package com.dezdeqness.presentation.features.animestats

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.dezdeqness.ShikimoriApp
import com.dezdeqness.core.page.StatsPage
import com.dezdeqness.di.subcomponents.AnimeStatsArgsModule
import com.dezdeqness.presentation.features.details.AnimeStatsTransferModel

@Composable
fun AnimeStatsStandalonePage(
    modifier: Modifier = Modifier,
    scoreList: List<AnimeStatsTransferModel>,
    statusesList: List<AnimeStatsTransferModel>,
    navController: NavHostController,
) {

    val context = LocalContext.current
    val animeSimilarComponent = remember {
        (context.applicationContext as ShikimoriApp).appComponent
            .animeStatsComponent()
            .argsModule(
                AnimeStatsArgsModule(
                    AnimeStatsArguments(
                        scoresArgument = scoreList,
                        statusesArgument = statusesList
                    )
                )
            )
            .build()
    }

    val viewModel =
        viewModel<AnimeStatsViewModel>(factory = animeSimilarComponent.viewModelFactory())

    StatsPage(
        modifier = modifier,
        state = viewModel.statsStateFlow,
        onBackPressed = {
            navController.popBackStack()
        }
    )
}
