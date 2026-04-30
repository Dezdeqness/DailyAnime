package com.dezdeqness.presentation.features.screenshotsviewer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.dezdeqness.ShikimoriApp
import com.dezdeqness.feature.screenshotsviewer.ScreenshotViewerActions
import com.dezdeqness.feature.screenshotsviewer.ScreenshotViewerPage
import com.dezdeqness.feature.screenshotsviewer.ScreenshotsViewModel
import com.dezdeqness.foundation.ui.utils.rememberSystemUiController

@Composable
fun ScreenshotsViewerStandalone(
    modifier: Modifier = Modifier,
    index: Int,
    screenshots: List<String>,
    navController: NavHostController,
) {
    val context = LocalContext.current
    val achievementsComponent = remember {
        (context.applicationContext as ShikimoriApp).appComponent
            .screenshotsViewerComponent()
            .build()
    }

    val controller = rememberSystemUiController()

    DisposableEffect(Unit) {
        controller?.hide(WindowInsetsCompat.Type.systemBars())
        controller?.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        onDispose {
            controller?.show(WindowInsetsCompat.Type.systemBars())
        }
    }

    val viewModel =
        viewModel<ScreenshotsViewModel>(factory = achievementsComponent.viewModelFactory())

    LaunchedEffect(screenshots, index) {
        viewModel.onScreenshotOpened(screenshots = screenshots, index = index)
    }

    ScreenshotViewerPage(
        modifier = modifier,
        stateFlow = viewModel.state,
        effectFlow = viewModel.effects,
        actions = object : ScreenshotViewerActions {
            override fun onShareButtonClicked() {
                viewModel.onShareButtonClicked()
            }

            override fun onDownloadButtonClicked() {
                viewModel.onDownloadButtonClicked()
            }

            override fun onScreenShotChanged(index: Int) {
                viewModel.onScreenShotChanged(index = index)
            }

            override fun onBackClicked() {
                navController.popBackStack()
            }

        }
    )
}
