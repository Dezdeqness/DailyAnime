package com.dezdeqness.presentation.features.routing

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.dezdeqness.R
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.domain.repository.SettingsRepository
import com.dezdeqness.getComponent
import com.dezdeqness.presentation.MainActivity
import com.dezdeqness.presentation.event.NavigateToMainFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class RoutingActivity : AppCompatActivity() {

    @Inject
    lateinit var settingsRepository: SettingsRepository

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

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
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AppTheme.colors.onPrimary)
                        .padding(horizontal = 8.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        color = AppTheme.colors.textPrimary,
                        textAlign = TextAlign.Center,
                        style = AppTheme.typography.displayMedium,
                    )
                    CircularProgressIndicator(
                        modifier = Modifier.padding(top = 24.dp)
                    )
                }
            }
        }


        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collect { event ->
                    when (event) {
                        is NavigateToMainFlow -> {
                            startActivity(Intent(this@RoutingActivity, MainActivity::class.java))
                            finish()
                        }

                        else -> {}
                    }
                }
            }
        }
    }
}
