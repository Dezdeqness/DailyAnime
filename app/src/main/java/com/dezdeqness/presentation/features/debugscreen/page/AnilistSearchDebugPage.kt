package com.dezdeqness.presentation.features.debugscreen.page

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dezdeqness.appComponent
import com.dezdeqness.foundation.ui.theme.AppTheme
import com.dezdeqness.presentation.AnimeDetails
import com.dezdeqness.presentation.features.animelist.SearchPageStandalone
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
        composable<AnimeDetails> {
            Text(
                text = "AniList details are not supported yet",
                color = AppTheme.colors.textPrimary,
                modifier = Modifier.padding(16.dp),
            )
        }
    }
}

@Serializable
private data object AnilistSearchRoute
