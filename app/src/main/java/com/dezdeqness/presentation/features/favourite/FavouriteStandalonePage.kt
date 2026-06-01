package com.dezdeqness.presentation.features.favourite

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.dezdeqness.ShikimoriApp
import com.dezdeqness.contract.favourite.model.FavouriteType
import com.dezdeqness.di.subcomponents.FavouriteArgsModule
import com.dezdeqness.feature.favourite.presentation.FavouritesActions
import com.dezdeqness.feature.favourite.presentation.FavouritesPage
import com.dezdeqness.feature.favourite.presentation.FavouritesViewModel
import com.dezdeqness.presentation.AnimeDetails
import com.dezdeqness.presentation.CharacterDetails
import com.dezdeqness.presentation.PersonDetails

private val PERSON_LIKE_TYPES = setOf(
    FavouriteType.PERSON,
    FavouriteType.MANGAKA,
    FavouriteType.SEYU,
    FavouriteType.PRODUCER,
)

@Composable
fun FavouriteStandalonePage(
    modifier: Modifier = Modifier,
    userId: Long,
    navController: NavHostController,
) {
    val context = LocalContext.current
    val favouritesComponent = remember {
        (context.applicationContext as ShikimoriApp).appComponent
            .favouriteComponent()
            .argsModule(FavouriteArgsModule(userId = userId))
            .build()
    }

    val viewModel =
        viewModel<FavouritesViewModel>(factory = favouritesComponent.viewModelFactory())

    val actions = remember {
        object : FavouritesActions {
            override fun onBackPressed() {
                navController.popBackStack()
            }

            override fun onItemClicked(id: Long, type: FavouriteType) {
                val destination: Any = when (type) {
                    FavouriteType.ANIME -> AnimeDetails(id)
                    FavouriteType.CHARACTER -> CharacterDetails(id)
                    in PERSON_LIKE_TYPES -> PersonDetails(id)
                    else -> return
                }
                navController.navigate(destination)
            }
        }
    }

    FavouritesPage(
        modifier = modifier,
        stateFlow = viewModel.favouritesState,
        actions = actions,
    )
}
