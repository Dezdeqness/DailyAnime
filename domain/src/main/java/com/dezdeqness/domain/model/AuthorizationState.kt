package com.dezdeqness.domain.model

sealed class AuthorizationState {
    object LoggedIn : AuthorizationState()
    object LoggedOut : AuthorizationState()
}
