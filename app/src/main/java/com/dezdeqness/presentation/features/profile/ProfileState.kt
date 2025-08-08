package com.dezdeqness.presentation.features.profile

import com.dezdeqness.core.AuthorizedUiState

data class ProfileState(
    val authorizedState: AuthorizedUiState = AuthorizedUiState.Pending,
    val userId: Long? = null,
    val avatar: String = "",
    val nickname: String = "",
)
