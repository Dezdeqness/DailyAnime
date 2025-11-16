package com.dezdeqness.presentation.features.authorization

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.dezdeqness.R
import com.dezdeqness.data.analytics.AnalyticsManager
import com.dezdeqness.data.analytics.model.AuthStatus
import com.dezdeqness.data.core.config.ConfigManager
import com.dezdeqness.databinding.ActivityAuthorizationBinding
import com.dezdeqness.di.subcomponents.AuthorizationArgsModule
import com.dezdeqness.getComponent
import com.dezdeqness.presentation.event.AuthUrl
import com.dezdeqness.presentation.event.AuthorizationSuccess
import com.dezdeqness.presentation.event.CloseAuthorization
import kotlinx.coroutines.launch
import javax.inject.Inject


class AuthorizationActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var configManager: ConfigManager

    @Inject
    lateinit var analyticsManager: AnalyticsManager

    private val authorizationViewModel by viewModels<AuthorizationViewModel>(
        factoryProducer = {
            viewModelFactory
        }
    )

    private lateinit var binding: ActivityAuthorizationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()

        application
            .getComponent()
            .authorizationComponent()
            .argsModule(
                AuthorizationArgsModule(
                    isLogin = intent.getBooleanExtra(
                        KEY_IS_LOGIN_FLOW,
                        true
                    )
                )
            )
            .build()
            .inject(this)

        super.onCreate(savedInstanceState)
        binding = ActivityAuthorizationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObservers()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleDeepLink(intent)
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                authorizationViewModel.authorizationStateFlow.collect { state ->
                    binding.loading.isVisible = state.isLoading
                }

            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                authorizationViewModel.events.collect { event ->
                    when (event) {
                        is CloseAuthorization -> {
                            Toast.makeText(
                                this@AuthorizationActivity,
                                R.string.general_no_internet_error,
                                Toast.LENGTH_LONG
                            )
                                .show()
                            finish()
                        }

                        is AuthUrl -> {
                            val uri = event.url.toUri()
                            val customTabsIntent = CustomTabsIntent
                                .Builder()
                                .build()

                            val intent = Intent(Intent.ACTION_VIEW, uri)
                            if (intent.resolveActivity(this@AuthorizationActivity.packageManager) != null) {
                                try {
                                    analyticsManager.authStatusTracked(AuthStatus.CustomTabOpen)
                                    customTabsIntent.launchUrl(this@AuthorizationActivity, uri)
                                } catch (_: ActivityNotFoundException) {
                                    analyticsManager.authStatusTracked(AuthStatus.NoAppToOpen)
                                    this@AuthorizationActivity.startActivity(
                                        Intent(
                                            Intent.ACTION_VIEW,
                                            uri
                                        )
                                    )
                                }
                            } else {
                                analyticsManager.authStatusTracked(AuthStatus.NoAppToOpen)
                                Toast
                                    .makeText(
                                        this@AuthorizationActivity,
                                        R.string.general_no_app_view,
                                        Toast.LENGTH_LONG
                                    )
                                    .show()
                                finish()
                            }
                        }

                        is AuthorizationSuccess -> {
                            setResult(Activity.RESULT_OK, intent)
                            finish()
                        }

                        else -> {}
                    }
                }
            }
        }
    }

    private fun handleDeepLink(intent: Intent) {
        intent.data?.let { uri ->
            authorizationViewModel.onHandleDeeplink(uri.toString())
        }
    }

    companion object {

        private const val KEY_IS_LOGIN_FLOW = "is_login_flow"

        fun loginIntent(context: Context) = newIntent(context, isLogin = true)

        fun signUpIntent(context: Context) = newIntent(context, isLogin = false)

        private fun newIntent(context: Context, isLogin: Boolean) =
            Intent(context, AuthorizationActivity::class.java).apply {
                putExtra(KEY_IS_LOGIN_FLOW, isLogin)
            }
    }

}
