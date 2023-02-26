package com.dezdeqness.presentation.features.authorization

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import com.dezdeqness.databinding.ActivityAuthorizationBinding
import com.dezdeqness.domain.repository.AccountRepository
import com.dezdeqness.getComponent
import java.util.regex.Pattern
import javax.inject.Inject

class AuthorizationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthorizationBinding

    @Inject
    protected lateinit var accountRepository: AccountRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        application
            .getComponent()
            .authorizationComponent()
            .create()
            .inject(this)

        super.onCreate(savedInstanceState)
        binding = ActivityAuthorizationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupWebView()

        val isLoginFlow = intent.getBooleanExtra(KEY_IS_LOGIN_FLOW, true)

        val url = if (isLoginFlow) {
            SHIKIMORI_SIGN_IN_URL
        } else {
            SHIKIMORI_SIGN_UP_URL
        }

        binding.webView.loadUrl(url)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val webView = binding.webView
        if (webView.canGoBack()) {
            webView.goBack()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        with(binding.webView) {
            stopLoading()
            destroy()
        }
    }

    private fun setupWebView() {
        with(binding.webView) {
            settings.apply {
                domStorageEnabled = true
                javaScriptEnabled = true
            }

            webViewClient = object : WebViewClient() {

                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?,
                ): Boolean {
                    interceptCode(request?.url?.toString().orEmpty())
                    return false
                }

                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    if (url == com.dezdeqness.data.BuildConfig.BASE_AUTHORIZATION_URL) {
                        view?.loadUrl(
                            accountRepository.getAuthorizationCodeUrl().getOrNull().orEmpty(),
                        )
                    }
                }

                private fun interceptCode(url: String) {
                    val matcher = Pattern.compile(SHIKIMORI_PATTERN).matcher(url)
                    if (matcher.find()) {
                        val authCode =
                            if (matcher.group().isNullOrEmpty()) ""
                            else url
                                .substring(url.lastIndexOf("/"))
                                .replaceFirst("/", "")

                        val intent = Intent().apply {
                            putExtra(KEY_AUTHORIZATION_CODE, authCode)
                        }
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    }
                }

            }
        }
    }

    companion object {

        private const val SHIKIMORI_PATTERN =
            "https?://(?:www\\.)?shikimori\\.one/oauth/authorize/(?:.*)"
        private const val SHIKIMORI_SIGN_UP_URL = "https://shikimori.one/users/sign_up"
        private const val SHIKIMORI_SIGN_IN_URL = "https://shikimori.one/users/sign_in"

        const val KEY_AUTHORIZATION_CODE = "authorization_code"

        private const val KEY_IS_LOGIN_FLOW = "is_login_flow"

        fun startActivity(
            authorizationObserver: ActivityResultLauncher<Intent>,
            context: Context,
            isLogin: Boolean,
        ) {
            val intent = Intent(context, AuthorizationActivity::class.java).apply {
                putExtra(KEY_IS_LOGIN_FLOW, isLogin)
            }
            authorizationObserver.launch(intent)
        }
    }

}
