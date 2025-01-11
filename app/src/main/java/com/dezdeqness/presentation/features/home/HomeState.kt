package com.dezdeqness.presentation.features.home

import androidx.compose.runtime.Immutable
import com.dezdeqness.presentation.features.home.composable.SectionUiModel

data class HomeState(
    val authorizedState: AuthorizedState = AuthorizedState(),
    val sectionsState: SectionsState = SectionsState()
)

@Immutable
data class SectionsState(
    val sections: List<SectionUiModel> = listOf(),
)

data class AuthorizedState(
    val isAuthorized: Boolean = false,
    val userName: String = "",
    val avatarUrl: String = "",
)
