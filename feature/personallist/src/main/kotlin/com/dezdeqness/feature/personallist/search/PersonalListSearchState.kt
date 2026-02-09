package com.dezdeqness.feature.personallist.search

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.dezdeqness.feature.personallist.R
import com.dezdeqness.feature.personallist.search.model.SearchUserRateUiModel

@Immutable
data class PersonalListSearchState(
    val query: String = "",
    val status: PersonalListSearchStatus = PersonalListSearchStatus.Initial,
    val selectedTab: PersonalListTab = PersonalListTab.All,
    val list: List<SearchUserRateUiModel> = emptyList(),
)

enum class PersonalListSearchStatus {
    Initial,
    Loading,
    Empty,
    Error,
    Loaded,
}

enum class PersonalListTab(@StringRes val displayNameRes: Int) {
    All(R.string.anime_personal_list_ribbon_status_all),
    Planned(R.string.anime_personal_list_ribbon_status_planned),
    Watching(R.string.anime_personal_list_ribbon_status_watching),
    Rewatching(R.string.anime_personal_list_ribbon_status_rewatching),
    Completed(R.string.anime_personal_list_ribbon_status_completed),
    OnHold(R.string.anime_personal_list_ribbon_status_on_hold),
    Dropped(R.string.anime_personal_list_ribbon_status_dropped),
}
