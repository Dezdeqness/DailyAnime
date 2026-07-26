package com.dezdeqness.presentation.features.authorization

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.runtime.getValue
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.dezdeqness.R
import com.dezdeqness.data.analytics.AnalyticsManager
import com.dezdeqness.data.analytics.model.AuthStatus
import com.dezdeqness.feature.auth.presentation.AuthorizationEffect
import com.dezdeqness.feature.auth.presentation.AuthorizationScreen
import com.dezdeqness.feature.auth.presentation.AuthorizationViewModel
import com.dezdeqness.foundation.ui.theme.AppTheme
import com.dezdeqness.getComponent
import javax.inject.Inject
import kotlinx.coroutines.launch

class AuthorizationActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var analyticsManager: AnalyticsManager

    private val authorizationViewModel by viewModels<AuthorizationViewModel>(
        factoryProducer = { viewModelFactory },
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()

        application
            .getComponent()
            .authorizationComponent()
            .create()
            .inject(this)

        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                val state by authorizationViewModel.authorizationStateFlow.collectAsStateWithLifecycle()
                AuthorizationScreen(isLoading = state.isLoading)
            }
        }

        observeEffects()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        intent.data?.let { authorizationViewModel.onHandleDeeplink(it.toString()) }
    }

    private fun observeEffects() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                authorizationViewModel.effects.collect { effect ->
                    when (effect) {
                        is AuthorizationEffect.OpenUrl -> openAuthorizationUrl(effect.url)

                        AuthorizationEffect.Success -> {
                            setResult(Activity.RESULT_OK, intent)
                            finish()
                        }

                        AuthorizationEffect.Close -> {
                            Toast.makeText(
                                this@AuthorizationActivity,
                                R.string.general_no_internet_error,
                                Toast.LENGTH_LONG,
                            ).show()
                            finish()
                        }
                    }
                }
            }
        }
    }

    private fun openAuthorizationUrl(url: String) {
        val uri = url.toUri()
        val viewIntent = Intent(Intent.ACTION_VIEW, uri)

        if (viewIntent.resolveActivity(packageManager) == null) {
            analyticsManager.authStatusTracked(AuthStatus.NoAppToOpen)
            Toast.makeText(this, R.string.general_no_app_view, Toast.LENGTH_LONG).show()
            finish()
            return
        }

        try {
            analyticsManager.authStatusTracked(AuthStatus.CustomTabOpen)
            CustomTabsIntent.Builder().build().launchUrl(this, uri)
        } catch (_: ActivityNotFoundException) {
            analyticsManager.authStatusTracked(AuthStatus.NoAppToOpen)
            startActivity(viewIntent)
        }
    }

    companion object {

        fun loginIntent(context: Context) = newIntent(context)

        fun signUpIntent(context: Context) = newIntent(context)

        private fun newIntent(context: Context) =
            Intent(context, AuthorizationActivity::class.java)
    }
}
