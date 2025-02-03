package com.dezdeqness.presentation.features.home

import androidx.compose.runtime.Immutable
import com.dezdeqness.presentation.features.home.model.HomeCalendarSectionUiModel
import com.dezdeqness.presentation.features.home.model.SectionUiModel

data class HomeState(
    val authorizedState: AuthorizedState = AuthorizedState(),
    val sectionsState: SectionsState = SectionsState()
)

@Immutable
data class SectionsState(
    val genreSections: List<SectionUiModel> = listOf(),
    val calendarSection: HomeCalendarSectionUiModel = HomeCalendarSectionUiModel()
)

data class AuthorizedState(
    val isAuthorized: Boolean = false,
    val userName: String = "",
    val avatarUrl: String = "",
)
