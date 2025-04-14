package com.dezdeqness.presentation.features.routing

import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.dezdeqness.R
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.domain.repository.SettingsRepository
import com.dezdeqness.getComponent
import com.dezdeqness.presentation.event.HandlePermission
import com.dezdeqness.presentation.event.NavigateToMainFlow
import com.dezdeqness.presentation.routing.ApplicationRouter
import kotlinx.coroutines.launch
import javax.inject.Inject

class RoutingActivity : AppCompatActivity() {

    @Inject
    lateinit var settingsRepository: SettingsRepository

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var applicationRouter: ApplicationRouter

    private val pushNotificationPermissionLauncher =
        registerForActivityResult(RequestPermission()) { granted ->
            viewModel.fetchData()
        }

    private val viewModel: RoutingViewModel by viewModels(
        factoryProducer = {
            viewModelFactory
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        application
            .getComponent()
            .routingComponent()
            .create()
            .inject(this)

        lifecycleScope.launch {
            val status = settingsRepository.getNightThemeStatus()
            if (status) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
        setContent {
            AppTheme {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AppTheme.colors.onPrimary)
                ) {
                    val compositionLoading by rememberLottieComposition(
                        LottieCompositionSpec.RawRes(R.raw.loading)
                    )

                    Text(
                        text = stringResource(id = R.string.app_name),
                        color = AppTheme.colors.textPrimary,
                        fontFamily = FontFamily(Font(R.font.pacifico_regular)),
                        textAlign = TextAlign.Center,
                        style = AppTheme.typography.displayMedium,
                    )

                    LottieAnimation(
                        composition = compositionLoading,
                        iterations = LottieConstants.IterateForever,
                        modifier = Modifier.height(120.dp)
                    )
                }
            }
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collect { event ->
                    when (event) {
                        is NavigateToMainFlow -> {
                            applicationRouter.navigateToMainScreen(this@RoutingActivity)
                            finish()
                        }

                        is HandlePermission -> {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                pushNotificationPermissionLauncher
                                    .launch(android.Manifest.permission.POST_NOTIFICATIONS)
                            } else {
                                viewModel.fetchData()
                            }
                        }

                        else -> {}
                    }
                }
            }
        }
    }
}
