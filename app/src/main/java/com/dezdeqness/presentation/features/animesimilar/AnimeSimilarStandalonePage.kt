package com.dezdeqness.presentation.features.animesimilar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.dezdeqness.R
import com.dezdeqness.ShikimoriApp
import com.dezdeqness.di.subcomponents.SimilarArgsModule
import com.dezdeqness.presentation.Details
import com.dezdeqness.presentation.action.Action
import com.dezdeqness.presentation.event.AnimeDetails
import com.dezdeqness.presentation.features.animesimilar.composable.SimilarItem
import com.dezdeqness.presentation.features.genericlistscreen.GenericListableScreen
import com.dezdeqness.presentation.features.genericlistscreen.GenericListableViewModel
import com.dezdeqness.presentation.features.genericlistscreen.GenericRenderer
import com.dezdeqness.presentation.models.AdapterItem
import com.dezdeqness.presentation.models.SimilarUiModel

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

    val viewModel = viewModel<GenericListableViewModel>(factory = animeSimilarComponent.viewModelFactory())

    val analyticsManager = animeSimilarComponent.analyticsManager()

    val renderer = remember {
        object : GenericRenderer {
            @Composable
            override fun Render(modifier: Modifier, item: AdapterItem, onClick: (Action) -> Unit) {
                if (item !is SimilarUiModel) return

                SimilarItem(
                    modifier = modifier,
                    item = item,
                    onClick = onClick,
                )
            }
        }
    }

    GenericListableScreen(
        modifier = modifier,
        renderer = renderer,
        titleRes = R.string.anime_similar_title,
        onEvent = { event ->
            when (event) {
                is AnimeDetails -> {
                    analyticsManager.detailsTracked(
                        id = event.animeId.toString(),
                        title = event.title
                    )
                    navController.navigate(Details(event.animeId))
                    true
                }

                else -> {
                    false
                }
            }
        },
        viewModel = viewModel,
        navController = navController,
    )

}
