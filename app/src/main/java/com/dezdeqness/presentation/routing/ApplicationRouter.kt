package com.dezdeqness.presentation.routing

import android.content.Context
import com.dezdeqness.presentation.MainActivity
import com.dezdeqness.presentation.features.authorization.AuthorizationActivity
import com.dezdeqness.presentation.features.screenshotsviewer.ScreenshotsViewerActivity
import javax.inject.Inject

class ApplicationRouter @Inject constructor() {

    fun navigateToMainScreen(context: Context) = with(context) {
        startActivity(
            MainActivity.newIntent(this)
        )
    }

    fun navigateToLoginScreen(context: Context) = with(context) {
        startActivity(
            AuthorizationActivity.loginIntent(context)
        )
    }

    fun navigateToSignUpScreen(context: Context) = with(context) {
        startActivity(
            AuthorizationActivity.signUpIntent(context)
        )
    }

    fun navigateToScreenshotViewerScreen(
        context: Context,
        screenshots: List<String>,
        index: Int,
    ) = with(context) {
        startActivity(
            ScreenshotsViewerActivity.newIntent(
                context = context,
                screenshots = screenshots,
                currentIndex = index,
            )
        )
    }

}
