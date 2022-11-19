package com.dezdeqness.presentation.features.authorization

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import com.dezdeqness.databinding.ActivityAuthorizationBinding
import com.dezdeqness.domain.repository.AccountRepository
import com.dezdeqness.getComponent
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

        binding.webView.loadUrl(accountRepository.getAuthorizationCodeUrl().getOrNull().orEmpty())
    }

    private fun setupWebView() {
        with(binding.webView) {
            settings.apply {
                this.javaScriptEnabled = true
            }

            webViewClient = object : WebViewClient() {

                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    if (!url.isNullOrBlank()) {
                        val uri = Uri.parse(url)
                        if (uri.pathSegments.size == 3) {
                            val authorizationCode = uri.lastPathSegment
                            val intent = Intent().apply {
                                putExtra(KEY_AUTHORIZATION_CODE, authorizationCode)
                            }
                            setResult(Activity.RESULT_OK, intent)
                            finish()
                        }
                    }
                }

            }
        }
    }

    companion object {

        const val KEY_AUTHORIZATION_CODE = "authorization_code"

        fun startActivity(authorizationObserver: ActivityResultLauncher<Intent>, context: Context) {
            val intent = Intent(context, AuthorizationActivity::class.java)
            authorizationObserver.launch(intent)
        }
    }

}
