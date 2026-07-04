package com.dezdeqness.presentation.features.animechronology

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.dezdeqness.ShikimoriApp
import com.dezdeqness.di.subcomponents.ChronologyArgsModule
import com.dezdeqness.feature.details.related.presentation.ChronologyListPage
import com.dezdeqness.feature.details.related.presentation.RelatedListActions
import com.dezdeqness.feature.details.related.presentation.RelatedListViewModel
import com.dezdeqness.presentation.AnimeDetails

@Composable
fun AnimeChronologyStandalonePage(
    modifier: Modifier = Modifier,
    animeId: Long,
    navController: NavHostController,
) {
    val context = LocalContext.current
    val animeChronologyComponent = remember {
        (context.applicationContext as ShikimoriApp).appComponent
            .animeChronologyComponent()
            .argsModule(ChronologyArgsModule(animeId))
            .build()
    }

    val viewModel =
        viewModel<RelatedListViewModel>(factory = animeChronologyComponent.viewModelFactory())

    val analyticsManager = animeChronologyComponent.analyticsManager()

    ChronologyListPage(
        modifier = modifier,
        stateFlow = viewModel.stateFlow,
        actions = object : RelatedListActions {
            override fun onAnimeClicked(animeId: Long, title: String) {
                analyticsManager.detailsTracked(
                    id = animeId.toString(),
                    title = title,
                )
                navController.navigate(AnimeDetails(animeId))
            }

            override fun onRetryClicked() {
                viewModel.onRetryClicked()
            }

            override fun onBackPressed() {
                navController.popBackStack()
            }
        },
    )
}
