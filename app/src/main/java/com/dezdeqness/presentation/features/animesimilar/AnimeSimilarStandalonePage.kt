package com.dezdeqness.presentation.features.animesimilar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.dezdeqness.ShikimoriApp
import com.dezdeqness.di.subcomponents.SimilarArgsModule
import com.dezdeqness.feature.details.related.presentation.RelatedListActions
import com.dezdeqness.feature.details.related.presentation.RelatedListViewModel
import com.dezdeqness.feature.details.related.presentation.SimilarListPage
import com.dezdeqness.presentation.AnimeDetails

@Composable
fun AnimeSimilarStandalonePage(
    modifier: Modifier = Modifier,
    animeId: Long,
    navController: NavHostController,
) {
    val context = LocalContext.current
    val animeSimilarComponent = remember {
        (context.applicationContext as ShikimoriApp).appComponent
            .animeSimilarComponent()
            .argsModule(SimilarArgsModule(animeId))
            .build()
    }

    val viewModel =
        viewModel<RelatedListViewModel>(factory = animeSimilarComponent.viewModelFactory())

    val analyticsManager = animeSimilarComponent.analyticsManager()

    SimilarListPage(
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
