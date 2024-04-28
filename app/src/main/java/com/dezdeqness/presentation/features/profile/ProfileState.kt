package com.dezdeqness.presentation.features.profile

data class ProfileState(
    val isAuthorized: Boolean = false,
    val avatar: String = "",
    val nickname: String = "",
)
