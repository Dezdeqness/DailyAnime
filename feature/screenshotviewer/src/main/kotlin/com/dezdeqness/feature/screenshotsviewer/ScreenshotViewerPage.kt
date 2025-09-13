package com.dezdeqness.feature.screenshotsviewer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ShareCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dezdeqness.core.ui.views.toolbar.AppToolbar
import com.dezdeqness.core.utils.collectEvents
import com.dezdeqness.feature.screenshotsviewer.composables.ScreenshotPager
import com.dezdeqness.feature.screenshotsviewer.store.ScreenshotsNamespace
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenshotViewerPage(
    modifier: Modifier = Modifier,
    stateFlow: StateFlow<ScreenshotsNamespace.State>,
    effectFlow: Flow<ScreenshotsNamespace.Effect>,
    actions: ScreenshotViewerActions,
) {
    Box(modifier = modifier.background(Color.Black)) {
        val context = LocalContext.current

        val state by stateFlow.collectAsStateWithLifecycle()
        val pagerState = rememberPagerState(initialPage = state.index) {
            state.screenshotsList.size
        }

        var isToolbarVisible by remember {
            mutableStateOf(true)
        }

        ScreenshotPager(
            state = pagerState,
            items = state.screenshotsList,
            onShow = {
                isToolbarVisible = true
                actions.onShowSystemUi()
            },
            onHide = {
                isToolbarVisible = false
                actions.onHideSystemUi()
            }
        )

        effectFlow
            .collectEvents { effect ->
                when (effect) {
                    is ScreenshotsNamespace.Effect.ShareUrl -> {
                        val url = effect.url
                        ShareCompat.IntentBuilder(context)
                            .setType("text/plain")
                            .setText(url)
                            .startChooser()
                    }
                }
            }

        LaunchedEffect(pagerState) {
            snapshotFlow { pagerState.currentPage }.collect { page ->
                actions.onScreenShotChanged(page)
            }
        }

        AnimatedVisibility(
            visible = isToolbarVisible,
            enter = fadeIn() + expandVertically(),
            exit = shrinkVertically() + fadeOut(),
        ) {
            AppToolbar(
                title = "${state.index + 1}/${state.screenshotsList.size}",
                titleColor = Color.White,
                colors = TopAppBarDefaults.topAppBarColors(
                    titleContentColor = Color.White,
                    containerColor = Color(0x29000000),
                ),
                navigationColor = Color.White,
                navigationClick = {
                    actions.onBackClicked()
                },
                actions = {
                    IconButton(
                        onClick = {
                            actions.onShareButtonClicked()
                        },
                    ) {
                        Icon(
                            Icons.Default.Share,
                            tint = Color.White,
                            contentDescription = null,
                        )
                    }
                },
            )
        }

    }
}
