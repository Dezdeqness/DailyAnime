package com.dezdeqness.domain.model

sealed class AuthorizationState {
    data object LoggedIn : AuthorizationState()
    data object LoggedOut : AuthorizationState()
}
