package com.dezdeqness.presentation.features.animechronology

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.dezdeqness.R
import com.dezdeqness.ShikimoriApp
import com.dezdeqness.di.subcomponents.ChronologyArgsModule
import com.dezdeqness.presentation.Details
import com.dezdeqness.presentation.action.Action
import com.dezdeqness.presentation.event.AnimeDetails
import com.dezdeqness.presentation.features.animechronology.composable.ChronologyItem
import com.dezdeqness.presentation.features.genericlistscreen.GenericListableScreen
import com.dezdeqness.presentation.features.genericlistscreen.GenericListableViewModel
import com.dezdeqness.presentation.features.genericlistscreen.GenericRenderer
import com.dezdeqness.presentation.models.AdapterItem
import com.dezdeqness.presentation.models.ChronologyUiModel

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

    val viewModel = viewModel<GenericListableViewModel>(factory = animeChronologyComponent.viewModelFactory())

    val analyticsManager = animeChronologyComponent.analyticsManager()

    val renderer = remember {
        object : GenericRenderer {
            @Composable
            override fun Render(modifier: Modifier, item: AdapterItem, onClick: (Action) -> Unit) {
                if (item !is ChronologyUiModel) return

                ChronologyItem(
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
        titleRes = R.string.anime_chronology_title,
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
