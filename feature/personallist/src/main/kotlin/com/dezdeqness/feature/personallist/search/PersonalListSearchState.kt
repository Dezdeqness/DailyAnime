package com.dezdeqness.feature.personallist.search

import androidx.compose.runtime.Immutable

@Immutable
data class PersonalListSearchState(
    val query: String = "",
    val status: PersonalListSearchStatus = PersonalListSearchStatus.Initial,
    val selectedTab: PersonalListTab = PersonalListTab.All,
    val list: List<UserRateUiModel> = emptyList(),
)

enum class PersonalListSearchStatus {
    Initial,
    Loading,
    Empty,
    Error,
    Loaded,
}

enum class PersonalListTab {
    All,
    Planned,
    Watching,
    Rewatching,
    Completed,
    OnHold,
    Dropped,
}
