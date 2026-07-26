package com.dezdeqness.presentation.features.unauthorized.host

import com.dezdeqness.shared.presentation.model.AuthorizedUiState

data class UnauthorizedHostState(
    val authorizedState: AuthorizedUiState = AuthorizedUiState.Pending,
)
