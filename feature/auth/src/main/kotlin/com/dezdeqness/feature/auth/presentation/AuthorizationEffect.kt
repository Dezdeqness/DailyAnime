package com.dezdeqness.feature.auth.presentation

sealed interface AuthorizationEffect {
    data class OpenUrl(val url: String) : AuthorizationEffect
    data object Success : AuthorizationEffect
    data object Close : AuthorizationEffect
}
