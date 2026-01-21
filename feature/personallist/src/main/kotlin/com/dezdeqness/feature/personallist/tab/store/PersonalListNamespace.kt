package com.dezdeqness.feature.personallist.tab.store

import com.dezdeqness.feature.personallist.model.UserRateUiModel

interface PersonalListNamespace {
    data class State(
        val personalListStatus: String = "",
        val list: List<UserRateUiModel> = listOf(),
        val dataStatus: PersonalListStatus = PersonalListStatus.Initial,
        val isPullDownRefreshing: Boolean = false,
        val currentPage: Int = 1,
        val hasNextPage: Boolean = false,
        val hasLocalChanges: Boolean = false,
        val hasPendingRefresh: Boolean = false,
    )

    sealed class Event {
        data class InitialLoad(val status: String) : Event()
        data object LoadMore : Event()
        data object Refresh : Event()
        data class OnPageLoaded(val list: List<UserRateUiModel>, val hasNextPage: Boolean) : Event()
        data class OnLoadMorePageLoaded(val list: List<UserRateUiModel>, val hasNextPage: Boolean) :
            Event()

        data class OnLoadMorePageError(val message: String, val error: Throwable) : Event()
        data class OnLoadPageError(val message: String, val error: Throwable) : Event()
        data class ItemRemovedLocally(val userRateId: Long) : Event()
        data object DismissRefreshHint : Event()
        data object MarkPendingRefresh : Event()
        data object CheckPendingRefresh : Event()
    }

    sealed class Effect {
        data object Error : Effect()
    }

    sealed class Command {
        data class LoadPage(val status: String, val page: Int, val isLoadMore: Boolean = false) :
            Command()
    }
}

enum class PersonalListStatus {
    Initial,
    Loading,
    Empty,
    Error,
    Loaded,
}
