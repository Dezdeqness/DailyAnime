package com.dezdeqness.presentation.features.screenshotsviewer

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.core.app.ShareCompat
import androidx.lifecycle.ViewModelProvider
import com.dezdeqness.R
import com.dezdeqness.core.utils.collectEvents
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.core.ui.views.toolbar.AppToolbar
import com.dezdeqness.di.subcomponents.ScreenshotsArgsModule
import com.dezdeqness.getComponent
import com.dezdeqness.presentation.features.screenshotsviewer.composables.ScreenshotPager
import com.dezdeqness.presentation.features.screenshotsviewer.store.ScreenshotsNamespace
import javax.inject.Inject

class ScreenshotsViewerActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: ScreenshotsViewModel by viewModels(
        factoryProducer = {
            viewModelFactory
        }
    )

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        application
            .getComponent()
            .screenshotsViewerComponent()
            .argsModule(
                ScreenshotsArgsModule(
                    index = intent.getIntExtra(KEY_CURRENT_INDEX, 0),
                    screenshots = intent.getStringArrayListExtra(KEY_SCREENSHOTS) ?: listOf(),
                )
            )
            .build()
            .inject(this)

        setContent {
            AppTheme {
                Box(modifier = Modifier.background(Color.Black)) {
                    val context = LocalContext.current

                    val state by viewModel.state.collectAsState()
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
                            showSystemUI()
                        },
                        onHide = {
                            isToolbarVisible = false
                            hideSystemUI()
                        }
                    )

                    viewModel
                        .effects
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
                            viewModel.onScreenShotChanged(page)
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
                                finish()
                            },
                            actions = {
                                IconButton(
                                    onClick = {
                                        viewModel.onShareButtonClicked()
                                    },
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_share),
                                        tint = Color.White,
                                        contentDescription = null,
                                    )
                                }
                            },
                        )
                    }

                }
            }
        }
    }

    companion object {

        private const val KEY_CURRENT_INDEX = "current_index"
        private const val KEY_SCREENSHOTS = "screenshots"

        fun newIntent(
            context: Context,
            currentIndex: Int,
            screenshots: List<String>,
        ) = Intent(context, ScreenshotsViewerActivity::class.java).apply {
            putExtra(KEY_CURRENT_INDEX, currentIndex)
            putStringArrayListExtra(KEY_SCREENSHOTS, ArrayList(screenshots))
        }

    }

}

fun Activity.showSystemUI() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        window.setDecorFitsSystemWindows(false)
        window.insetsController?.show(WindowInsets.Type.systemBars())
    } else {
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }
}

fun Activity.hideSystemUI() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        window.insetsController?.let {
            it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            it.hide(WindowInsets.Type.systemBars())
        }
    } else {
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
    }
}
