package com.dezdeqness.feature.home.presentation.preview

import com.dezdeqness.feature.history.presentation.models.HistoryModel
import com.dezdeqness.feature.home.presentation.AuthorizedState
import com.dezdeqness.feature.home.presentation.HomeActions
import com.dezdeqness.feature.home.presentation.HomeState
import com.dezdeqness.feature.home.presentation.LatestHistoryItemSection
import com.dezdeqness.feature.home.presentation.SectionsState
import com.dezdeqness.feature.home.presentation.models.HomeCalendarSectionUiModel
import com.dezdeqness.feature.home.presentation.models.HomeCalendarUiModel
import com.dezdeqness.feature.home.presentation.models.SectionAnimeUiModel
import com.dezdeqness.feature.home.presentation.models.SectionStatus
import com.dezdeqness.feature.home.presentation.models.SectionUiModel

object HomePreviewData {

    private val sectionItems = listOf(
        SectionAnimeUiModel(id = 1L, title = "Стальной алхимик", logoUrl = ""),
        SectionAnimeUiModel(id = 2L, title = "Наруто", logoUrl = ""),
        SectionAnimeUiModel(id = 3L, title = "Атака титанов", logoUrl = ""),
    )

    private val calendarItems = listOf(
        HomeCalendarUiModel(
            id = 4L,
            title = "Ван-Пис",
            description = "Эпизод 1122 выйдет в воскресенье",
            imageUrl = "",
        ),
        HomeCalendarUiModel(
            id = 5L,
            title = "Блич",
            description = "Эпизод 14 выйдет в субботу",
            imageUrl = "",
        ),
    )

    val loadedState = HomeState(
        authorizedState = AuthorizedState(
            isAuthorized = true,
            userName = "Astaroth",
            avatarUrl = "",
        ),
        sectionsState = SectionsState(
            genreSections = listOf(
                SectionUiModel(
                    id = "shounen",
                    numericId = "27",
                    title = "Сёнен",
                    items = sectionItems,
                    status = SectionStatus.Loaded,
                ),
            ),
            calendarSection = HomeCalendarSectionUiModel(
                items = calendarItems,
                status = SectionStatus.Loaded,
                isCalendarActionVisible = true,
            ),
            latestHistoryItem = LatestHistoryItemSection(
                historyUiModel = HistoryModel.HistoryUiModel(
                    name = "Стальной алхимик",
                    action = "Просмотрен 51 эпизод",
                    imageUrl = "",
                ),
                status = SectionStatus.Loaded,
            ),
        ),
    )

    val loadingState = HomeState(
        authorizedState = AuthorizedState(),
        sectionsState = SectionsState(
            genreSections = listOf(
                SectionUiModel(
                    id = "shounen",
                    numericId = "27",
                    title = "Сёнен",
                    status = SectionStatus.Loading,
                ),
            ),
        ),
    )

    val emptyActions = object : HomeActions {
        override fun onInitialLoad() = Unit
        override fun onAnimeClicked(animeId: Long, title: String) = Unit
        override fun onCalendarHeaderClicked() = Unit
        override fun onHistoryHeaderClicked() = Unit
    }
}
