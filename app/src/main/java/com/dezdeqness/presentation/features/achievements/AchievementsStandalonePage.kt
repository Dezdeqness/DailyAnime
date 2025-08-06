package com.dezdeqness.presentation.features.achievements

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.dezdeqness.ShikimoriApp
import com.dezdeqness.di.subcomponents.AchievementsArgsModule
import com.dezdeqness.feature.achievements.presentation.AchievementsPage
import com.dezdeqness.feature.achievements.presentation.AchievementsViewModel

@Composable
fun AchievementsStandalonePage(
    modifier: Modifier = Modifier,
    userId: Long,
    navController: NavHostController,
) {
    val context = LocalContext.current
    val achievementsComponent = remember {
        (context.applicationContext as ShikimoriApp).appComponent
            .achievementsComponent()
            .argsModule(AchievementsArgsModule(userId = userId))
            .build()
    }

    val viewModel = viewModel<AchievementsViewModel>(factory = achievementsComponent.viewModelFactory())

    val state = viewModel.achievementsState.collectAsStateWithLifecycle()

    println(state.toString())

    AchievementsPage(modifier = modifier)
}
