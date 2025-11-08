package com.dezdeqness.presentation.routing

import android.content.Context
import com.dezdeqness.presentation.MainActivity
import com.dezdeqness.presentation.features.authorization.AuthorizationActivity
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
}
