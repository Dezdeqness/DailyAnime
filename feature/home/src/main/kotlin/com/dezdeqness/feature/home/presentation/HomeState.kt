package com.dezdeqness.feature.home.presentation

import androidx.compose.runtime.Immutable
import com.dezdeqness.feature.history.presentation.models.HistoryModel
import com.dezdeqness.feature.home.presentation.models.HomeCalendarSectionUiModel
import com.dezdeqness.feature.home.presentation.models.SectionStatus
import com.dezdeqness.feature.home.presentation.models.SectionUiModel

data class HomeState(
    val authorizedState: AuthorizedState = AuthorizedState(),
    val sectionsState: SectionsState = SectionsState(),
)

@Immutable
data class SectionsState(
    val genreSections: List<SectionUiModel> = listOf(),
    val calendarSection: HomeCalendarSectionUiModel = HomeCalendarSectionUiModel(),
    val latestHistoryItem: LatestHistoryItemSection = LatestHistoryItemSection(),
)

data class AuthorizedState(
    val isAuthorized: Boolean = false,
    val userName: String = "",
    val avatarUrl: String = "",
)

data class LatestHistoryItemSection(
    val historyUiModel: HistoryModel.HistoryUiModel? = null,
    val status: SectionStatus = SectionStatus.Initial,
)
