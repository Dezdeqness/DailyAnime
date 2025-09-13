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
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.lifecycle.ViewModelProvider
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.di.subcomponents.ScreenshotsArgsModule
import com.dezdeqness.feature.screenshotsviewer.ScreenshotViewerActions
import com.dezdeqness.feature.screenshotsviewer.ScreenshotViewerPage
import com.dezdeqness.feature.screenshotsviewer.ScreenshotsViewModel
import com.dezdeqness.getComponent
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
        enableEdgeToEdge()

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
                ScreenshotViewerPage(
                    stateFlow = viewModel.state,
                    effectFlow = viewModel.effects,
                    actions = object : ScreenshotViewerActions {
                        override fun onShareButtonClicked() {
                            viewModel.onShareButtonClicked()
                        }

                        override fun onShowSystemUi() {
                            hideSystemUI()
                        }

                        override fun onHideSystemUi() {
                            showSystemUI()
                        }

                        override fun onScreenShotChanged(index: Int) {
                            viewModel.onScreenShotChanged(index = index)
                        }

                        override fun onBackClicked() {
                            finish()
                        }

                    }
                )
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
