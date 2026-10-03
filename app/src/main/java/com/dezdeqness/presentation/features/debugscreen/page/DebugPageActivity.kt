package com.dezdeqness.presentation.features.debugscreen.page

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.ui.Modifier
import com.dezdeqness.foundation.ui.theme.AppTheme

class DebugPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val page = intent.getStringExtra(EXTRA_PAGE)?.let(DebugPage::valueOf)
        if (page == null) {
            finish()
            return
        }

        setContent {
            AppTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AppTheme.colors.onPrimary)
                        .safeDrawingPadding(),
                ) {
                    when (page) {
                        DebugPage.ANILIST_SEARCH -> AnilistSearchDebugPage()
                    }
                }
            }
        }
    }

    companion object {
        private const val EXTRA_PAGE = "page"

        fun newIntent(context: Context, page: DebugPage) = Intent(context, DebugPageActivity::class.java)
            .putExtra(EXTRA_PAGE, page.name)
    }
}
