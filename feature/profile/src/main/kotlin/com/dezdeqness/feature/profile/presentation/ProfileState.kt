package com.dezdeqness.feature.profile.presentation

import com.dezdeqness.shared.presentation.model.AuthorizedUiState

data class ProfileState(
    val authorizedState: AuthorizedUiState = AuthorizedUiState.Pending,
    val userId: Long? = null,
    val avatar: String = "",
    val nickname: String = "",
)
