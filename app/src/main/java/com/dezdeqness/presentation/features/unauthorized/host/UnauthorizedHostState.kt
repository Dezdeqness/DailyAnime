package com.dezdeqness.presentation.features.unauthorized.host

import com.dezdeqness.core.AuthorizedUiState


data class UnauthorizedHostState(
    val authorizedState: AuthorizedUiState = AuthorizedUiState.Pending,
)
