package com.dezdeqness.presentation.features.debugscreen.page

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.dezdeqness.appComponent
import com.dezdeqness.feature.stats.presentation.anime.deserializeListFromString
import com.dezdeqness.presentation.AnimeDetails
import com.dezdeqness.presentation.DetailsStats
import com.dezdeqness.presentation.features.animedetails.AnimeDetailsStandalonePage
import com.dezdeqness.presentation.features.animelist.SearchPageStandalone
import com.dezdeqness.presentation.features.animestats.AnimeStatsStandalonePage
import kotlinx.serialization.Serializable

@Composable
fun AnilistSearchDebugPage() {
    val context = LocalContext.current
    val anilistComponent = remember { context.appComponent.anilistComponent().create() }
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AnilistSearchRoute,
        modifier = Modifier.fillMaxSize(),
    ) {
        composable<AnilistSearchRoute> {
            SearchPageStandalone(
                navController = navController,
                sourceComponent = anilistComponent,
                modifier = Modifier.fillMaxSize(),
            )
        }
        composable<AnimeDetails> { backStackEntry ->
            AnimeDetailsStandalonePage(
                animeId = backStackEntry.toRoute<AnimeDetails>().id,
                navController = navController,
                sourceComponent = anilistComponent,
                modifier = Modifier.fillMaxSize(),
            )
        }
        composable<DetailsStats> { backStackEntry ->
            val route = backStackEntry.toRoute<DetailsStats>()
            AnimeStatsStandalonePage(
                scoreList = deserializeListFromString(route.scoreString),
                statusesList = deserializeListFromString(route.statusesString),
                navController = navController,
                sourceComponent = anilistComponent,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Serializable
private data object AnilistSearchRoute
