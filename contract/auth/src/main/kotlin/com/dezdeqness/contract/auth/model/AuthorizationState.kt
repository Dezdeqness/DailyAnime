package com.dezdeqness.contract.auth.model

sealed class AuthorizationState {
    data object LoggedIn : AuthorizationState()
    data object LoggedOut : AuthorizationState()
}
