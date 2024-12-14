package com.dezdeqness.presentation.features.home

import com.dezdeqness.presentation.features.home.composable.SectionUiModel

data class HomeState(
    val authorizedState: AuthorizedState = AuthorizedState(),
    val sectionsState: SectionsState = SectionsState()
)

data class SectionsState(
    val sections: List<SectionUiModel> = listOf(),
)

data class AuthorizedState(
    val isAuthorized: Boolean = false,
    val userName: String = "",
    val avatarUrl: String = "",
)
