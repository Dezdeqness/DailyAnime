package com.dezdeqness.presentation.features.screenshotsviewer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.repeatOnLifecycle
import com.dezdeqness.R
import com.dezdeqness.core.activity.hideSystemUI
import com.dezdeqness.core.activity.showSystemUI
import com.dezdeqness.di.subcomponents.ScreenshotsArgsModule
import com.dezdeqness.getComponent
import com.dezdeqness.presentation.event.ConsumableEvent
import com.dezdeqness.presentation.event.EventConsumer
import com.dezdeqness.presentation.event.PagerScrollToPage
import com.dezdeqness.presentation.features.screenshotsviewer.composables.ScreenshotPager
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ScreenshotsViewerActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: ScreenshotsViewModel by viewModels(
        factoryProducer = {
            viewModelFactory
        }
    )

    private val eventConsumer: EventConsumer by lazy {
        EventConsumer(context = this,)
    }


    @OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
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
            MaterialTheme {
                Box(modifier = Modifier.background(Color.Black)) {

                    val state by viewModel.screenshotState.collectAsState()
                    val pagerState = rememberPagerState { state.screenshots.size }

                    var isToolbarVisible by remember {
                        mutableStateOf(true)
                    }

                    ScreenshotPager(
                        state = pagerState,
                        items = state.screenshots,
                        onShow = {
                            isToolbarVisible = true
                            showSystemUI()
                        },
                        onHide = {
                            isToolbarVisible = false
                            hideSystemUI()
                        }
                    )

                    viewModel.events.collectEvents { event ->
                        when (event) {
                            is PagerScrollToPage -> {
                                pagerState.scrollToPage(event.index)
                            }
                            is ConsumableEvent -> {
                                eventConsumer.consume(event)
                            }
                            else -> {}
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
                        TopAppBar(
                            title = {
                                Text(text = "${state.currentIndex + 1}/${state.screenshots.size}")
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                titleContentColor = Color.White,
                                containerColor = Color(0x29000000),
                            ),
                            navigationIcon = {
                                IconButton(
                                    onClick = {
                                        finish()
                                    },
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_back),
                                        tint = Color.White,
                                        contentDescription = null,
                                    )
                                }
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

        fun startActivity(
            context: Context,
            currentIndex: Int,
            screenshots: ArrayList<String>,
        ) {
            val intent = Intent(context, ScreenshotsViewerActivity::class.java).apply {
                putExtra(KEY_CURRENT_INDEX, currentIndex)
                putStringArrayListExtra(KEY_SCREENSHOTS, screenshots)
            }
            context.startActivity(intent)
        }
    }

}

@Composable
fun <T> Flow<T>.collectEvents(
    lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
    sideEffect: (suspend (event: T) -> Unit)
) {
    val sideEffectFlow = this
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(sideEffectFlow, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(lifecycleState) {
            sideEffectFlow.collect {
                sideEffect(it)
            }
        }
    }
}
