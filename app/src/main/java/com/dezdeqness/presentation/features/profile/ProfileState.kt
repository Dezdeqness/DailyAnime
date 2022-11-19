package com.dezdeqness.presentation.features.profile

data class ProfileState(
    val avatar: String = "",
    val isAuthorized: Boolean = false,
    val nickname: String = "",
)
