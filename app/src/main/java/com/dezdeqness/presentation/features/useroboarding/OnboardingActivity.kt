package com.dezdeqness.presentation.features.useroboarding

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.dezdeqness.R
import com.dezdeqness.contract.settings.models.NightThemePreference
import com.dezdeqness.contract.settings.models.ThemeMode
import com.dezdeqness.feature.onboarding.flow.presentation.OnboardingType
import com.dezdeqness.getComponent
import com.dezdeqness.presentation.AppContentTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class OnboardingActivity : AppCompatActivity() {

    private val themeModeFlow = MutableStateFlow<ThemeMode?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val onboardingType = intent.getStringExtra(EXTRA_TYPE)
            ?.let { runCatching { OnboardingType.valueOf(it) }.getOrNull() }
            ?: OnboardingType.Full

        lifecycleScope.launch {
            val mode = application
                .getComponent()
                .settingsRepository()
                .getPreference(NightThemePreference)

            themeModeFlow.value = mode

            val nightMode = when (mode) {
                ThemeMode.SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                ThemeMode.DARK, ThemeMode.AMOLED -> AppCompatDelegate.MODE_NIGHT_YES
                ThemeMode.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
            }
            AppCompatDelegate.setDefaultNightMode(nightMode)
        }

        setContent {
            val themeMode by themeModeFlow.collectAsStateWithLifecycle()

            AppContentTheme(themeMode = themeMode) {
                OnboardingStandalonePage(
                    modifier = Modifier.fillMaxSize(),
                    type = onboardingType,
                    onFinished = ::closeWithSlideDown,
                )
            }
        }
    }

    private fun closeWithSlideDown() {
        finish()
        @Suppress("DEPRECATION")
        overridePendingTransition(0, R.anim.onboarding_slide_out_down)
    }

    companion object {
        private const val EXTRA_TYPE = "onboarding_type"

        fun newIntent(
            context: Context,
            type: OnboardingType = OnboardingType.Full,
        ) = Intent(context, OnboardingActivity::class.java)
            .putExtra(EXTRA_TYPE, type.name)
    }
}
