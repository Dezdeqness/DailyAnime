package com.dezdeqness.presentation.features.useroboarding.selectgenres

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.dezdeqness.ShikimoriApp
import com.dezdeqness.core.utils.collectEvents
import com.dezdeqness.feature.onboarding.selectgenres.presentation.SelectGenresActions
import com.dezdeqness.feature.onboarding.selectgenres.presentation.SelectGenresContentPage
import com.dezdeqness.feature.onboarding.selectgenres.presentation.SelectGenresEvent
import com.dezdeqness.feature.onboarding.selectgenres.presentation.SelectGenresViewModel

@Composable
fun SelectGenresStandalonePage(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    val context = LocalContext.current
    val selectGenresComponent = remember {
        (context.applicationContext as ShikimoriApp).appComponent
            .selectGenresComponent()
            .create()
    }

    val viewModel =
        viewModel<SelectGenresViewModel>(factory = selectGenresComponent.viewModelFactory())

    val actions = remember {
        object : SelectGenresActions {
            override fun onBackPressed() {
                navController.popBackStack()
            }

            override fun onGenreClicked(genreId: String) {
                viewModel.onGenreClick(genreId)
            }

            override fun onSaveClicked() {
                viewModel.onSaveClick()
            }
        }
    }

    SelectGenresContentPage(
        modifier = modifier,
        stateFlow = viewModel.uiState,
        actions = actions,
    )

    viewModel.events.collectEvents { event ->
        when (event) {
            is SelectGenresEvent.Close -> {
                navController.popBackStack()
            }
        }
    }
}
