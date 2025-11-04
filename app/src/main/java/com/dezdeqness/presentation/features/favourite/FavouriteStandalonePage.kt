package com.dezdeqness.presentation.features.favourite

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.dezdeqness.ShikimoriApp
import com.dezdeqness.di.subcomponents.FavouriteArgsModule
import com.dezdeqness.feature.favourite.presentation.FavouritesActions
import com.dezdeqness.feature.favourite.presentation.FavouritesPage
import com.dezdeqness.feature.favourite.presentation.FavouritesViewModel

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
        }
    }

    FavouritesPage(
        modifier = modifier,
        stateFlow = viewModel.favouritesState,
        actions = actions,
    )
}
