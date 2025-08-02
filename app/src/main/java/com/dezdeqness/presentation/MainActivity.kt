package com.dezdeqness.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.dezdeqness.R
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.core.utils.collectEvents
import com.dezdeqness.data.analytics.AnalyticsManager
import com.dezdeqness.data.core.config.ConfigManager
import com.dezdeqness.domain.model.InitialSection
import com.dezdeqness.getComponent
import com.dezdeqness.presentation.event.LanguageDisclaimer
import com.dezdeqness.presentation.features.animechronology.AnimeChronologyStandalonePage
import com.dezdeqness.presentation.features.animelist.SearchPageStandalone
import com.dezdeqness.presentation.features.animesimilar.AnimeSimilarStandalonePage
import com.dezdeqness.presentation.features.animestats.AnimeStatsStandalonePage
import com.dezdeqness.presentation.features.calendar.CalendarStandalonePage
import com.dezdeqness.presentation.features.details.DetailsStandalonePage
import com.dezdeqness.presentation.features.details.Target
import com.dezdeqness.presentation.features.details.deserializeListFromString
import com.dezdeqness.presentation.features.history.HistoryStandalonePage
import com.dezdeqness.presentation.features.home.HomePageStandalone
import com.dezdeqness.presentation.features.personallist.host.PersonalHostStandalonePage
import com.dezdeqness.presentation.features.profile.ProfilePageStandalone
import com.dezdeqness.presentation.features.settings.SettingsPageStandalone
import com.dezdeqness.presentation.features.stats.StatsStandalonePage
import com.dezdeqness.presentation.models.MessageEvent.MessageEventStatus
import com.dezdeqness.ui.CustomSnackbarVisuals
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var analyticsManager: AnalyticsManager

    @Inject
    lateinit var configManager: ConfigManager

    private val mainViewModel by viewModels<MainViewModel>(
        factoryProducer = {
            viewModelFactory
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        application
            .getComponent()
            .mainComponent()
            .create()
            .inject(this)

        lifecycleScope.launch {
            val status = application.getComponent().settingsRepository().getNightThemeStatus()
            if (status) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        val section =
            runBlocking(application.getComponent().coroutineDispatcherProvider().io()) {
                application.getComponent().settingsRepository().getSelectedInitialSection()
            }

        setContent {
            val rootController = rememberNavController()
            val snackbarHostState = remember { SnackbarHostState() }

            val coroutineScope = rememberCoroutineScope()

            AppTheme {
                Box(modifier = Modifier.fillMaxSize()) {

                    NavHost(
                        navController = rootController,
                        startDestination = RootRoute,
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        composable<RootRoute> {
                            val navController = rememberNavController()

                            Scaffold(
                                contentWindowInsets = WindowInsets(0.dp),
                                bottomBar = {
                                    NavigationBar(
                                        containerColor = MaterialTheme.colorScheme.background,
                                        tonalElevation = 0.dp,
                                    ) {
                                        val navBackStackEntry =
                                            navController.currentBackStackEntryAsState().value
                                        val currentDestination = navBackStackEntry?.destination

                                        AppBottomTabModel.entries.forEach { item ->
                                            val isSelected =
                                                currentDestination?.hierarchy?.any {
                                                    it.hasRoute(
                                                        item.route::class
                                                    )
                                                } == true

                                            if (item == AppBottomTabModel.CALENDAR && configManager.isCalendarEnabled.not()) {
                                                return@forEach
                                            }
                                            NavigationBarItem(
                                                selected = isSelected,
                                                onClick = {
                                                    if (currentDestination != item.route) {
                                                        navController.navigate(item.route) {
                                                            popUpTo(navController.graph.findStartDestination().id) {
                                                                saveState = true
                                                            }
                                                            launchSingleTop = true
                                                            restoreState = true
                                                        }
                                                    }
                                                },
                                                icon = {
                                                    Icon(
                                                        painterResource(item.resIcon),
                                                        contentDescription = null,
                                                        modifier = Modifier.size(24.dp),
                                                    )
                                                },
                                            )
                                        }
                                    }
                                },
                                containerColor = AppTheme.colors.background,
                            ) { padding ->
                                NavHost(
                                    navController = navController,
                                    startDestination = sectionToRoute(section),
                                    modifier = Modifier
                                        .padding(padding)
                                        .fillMaxSize()
                                ) {
                                    composable<BottomBarNav.PersonalList> {
                                        PersonalHostStandalonePage(
                                            modifier = Modifier.fillMaxSize(),
                                            navController = rootController,
                                        )
                                    }
                                    composable<BottomBarNav.Home> {
                                        HomePageStandalone(
                                            modifier = Modifier.fillMaxSize(),
                                            navController = navController,
                                            rootController = rootController,
                                        )
                                    }
                                    composable<BottomBarNav.Calendar> {
                                        CalendarStandalonePage(
                                            modifier = Modifier.fillMaxSize(),
                                            navController = rootController,
                                        )
                                    }
                                    composable<BottomBarNav.Search> {
                                        SearchPageStandalone(
                                            modifier = Modifier.fillMaxSize(),
                                            navController = rootController,
                                        )
                                    }
                                    composable<BottomBarNav.Profile> {
                                        ProfilePageStandalone(
                                            modifier = Modifier.fillMaxSize(),
                                            navController = rootController,
                                        )
                                    }
                                }
                            }

                            mainViewModel.events.collectEvents { event ->
                                when (event) {
                                    is LanguageDisclaimer -> {
                                        showLanguageDisclaimer()
                                    }

                                    else -> {}
                                }
                            }
                        }

                        composable<History> {
                            HistoryStandalonePage(
                                modifier = Modifier.fillMaxSize(),
                                navController = rootController,
                            )
                        }
                        composable<Settings> {
                            SettingsPageStandalone(
                                modifier = Modifier.fillMaxSize(),
                                navController = rootController,
                            )
                        }
                        composable<Stats> {
                            StatsStandalonePage(
                                modifier = Modifier.fillMaxSize(),
                                navController = rootController,
                            )
                        }
                        composable<Details> { backStackEntry ->
                            val details = backStackEntry.toRoute<Details>()

                            val target = if (details.isAnime) {
                                Target.Anime(details.id)
                            } else {
                                Target.Character(details.id)
                            }

                            DetailsStandalonePage(
                                modifier = Modifier.fillMaxSize(),
                                navController = rootController,
                                target = target,
                            )
                        }

                        composable<Similar> { backStackEntry ->
                            val similar = backStackEntry.toRoute<Similar>()

                            AnimeSimilarStandalonePage(
                                modifier = Modifier.fillMaxSize(),
                                navController = rootController,
                                animeId = similar.id,
                            )
                        }

                        composable<Chronology> { backStackEntry ->
                            val chronology = backStackEntry.toRoute<Chronology>()

                            AnimeChronologyStandalonePage(
                                modifier = Modifier.fillMaxSize(),
                                navController = rootController,
                                animeId = chronology.id,
                            )
                        }

                        composable<DetailsStats> { backStackEntry ->
                            val detailsStats = backStackEntry.toRoute<DetailsStats>()

                            val scoreList = deserializeListFromString(detailsStats.scoreString)
                            val statusesList =
                                deserializeListFromString(detailsStats.statusesString)

                            AnimeStatsStandalonePage(
                                modifier = Modifier.fillMaxSize(),
                                navController = rootController,
                                scoreList = scoreList,
                                statusesList = statusesList,
                            )
                        }
                    }

                    SnackbarHost(
                        hostState = snackbarHostState,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 56.dp),
                        snackbar = { data ->
                            val visuals = (data.visuals as? CustomSnackbarVisuals)
                                ?: return@SnackbarHost

                            val color = when (visuals.messageStatus) {
                                MessageEventStatus.Success -> AppTheme.colors.success
                                MessageEventStatus.Error -> AppTheme.colors.error
                                MessageEventStatus.Unknown -> AppTheme.colors.background
                            }

                            Snackbar(
                                snackbarData = data,
                                containerColor = color,
                                actionColor = Color.Transparent,
                                contentColor = Color.White,
                            )
                        }
                    )
                }

                StatusBarProtection()

                mainViewModel.messageState.collectEvents { event ->
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            CustomSnackbarVisuals(
                                message = event.text,
                                messageStatus = event.status,
                            )
                        )
                    }
                }
            }
        }
    }

    private fun sectionToRoute(section: InitialSection) =
        when (section) {
            InitialSection.FAVORITES -> BottomBarNav.PersonalList
            InitialSection.HOME -> BottomBarNav.Home
            InitialSection.CALENDAR -> BottomBarNav.Calendar
            InitialSection.SEARCH -> BottomBarNav.Search
            InitialSection.PROFILE -> BottomBarNav.Profile
        }

    private fun showLanguageDisclaimer() {
        val dialog = MaterialAlertDialogBuilder(this)
            .setTitle(resources.getString(R.string.language_disclaimer_title))
            .setMessage(resources.getString(R.string.language_disclaimer_description))
            .setPositiveButton(resources.getString(R.string.language_disclaimer_positive)) { _, _ ->

            }
            .show()
        dialog.setCanceledOnTouchOutside(false)
    }

    enum class AppBottomTabModel(
        val route: BottomBarNav,
        @DrawableRes val resIcon: Int
    ) {
        PERSONAL_LIST(
            route = BottomBarNav.PersonalList,
            resIcon = R.drawable.ic_personal_list_filled,
        ),
        HOME(
            route = BottomBarNav.Home,
            resIcon = R.drawable.ic_personal_home_filled,
        ),
        CALENDAR(
            route = BottomBarNav.Calendar,
            resIcon = R.drawable.ic_calendar_filled,
        ),
        SEARCH(
            route = BottomBarNav.Search,
            resIcon = R.drawable.ic_search_v2,
        ),
        PROFILE(
            route = BottomBarNav.Profile,
            resIcon = R.drawable.ic_user_filled,
        ),
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, MainActivity::class.java)
    }

}

@Composable
private fun StatusBarProtection(
    color: Color = MaterialTheme.colorScheme.surfaceContainer,
    heightProvider: () -> Float = calculateGradientHeight(),
) {

    Canvas(Modifier.fillMaxSize()) {
        val calculatedHeight = heightProvider()
        val gradient = Brush.verticalGradient(
            colors = listOf(
                color.copy(alpha = 1f),
                color.copy(alpha = .8f),
                Color.Transparent
            ),
            startY = 0f,
            endY = calculatedHeight
        )
        drawRect(
            brush = gradient,
            size = Size(size.width, calculatedHeight),
        )
    }
}

@Composable
private fun calculateGradientHeight(): () -> Float {
    val statusBars = WindowInsets.statusBars
    val density = LocalDensity.current
    return { statusBars.getTop(density).times(1.2f) }
}
