package com.dezdeqness.feature.personallist.tab.store

import com.dezdeqness.feature.personallist.model.UserRateUiModel
import com.dezdeqness.feature.userrate.EditRateUiModel

interface PersonalListNamespace {
    data class State(
        val personalListStatus: String = "",
        val list: List<UserRateUiModel> = listOf(),
        val dataStatus: PersonalListStatus = PersonalListStatus.Initial,
        val isPullDownRefreshing: Boolean = false,
        val currentPage: Int = 1,
        val hasNextPage: Boolean = false,
    )

    sealed class Event {
        data class InitialLoad(val status: String) : Event()
        data object LoadMore : Event()
        data object Refresh : Event()
        data class UserRateChanged(val userRate: EditRateUiModel?, val statusId: String) : Event()
        data class UserRateIncrement(val userRateId: Long, val statusId: String) : Event()
        data class OnPageLoaded(val list: List<UserRateUiModel>, val hasNextPage: Boolean) : Event()
        data class OnLoadMorePageLoaded(val list: List<UserRateUiModel>, val hasNextPage: Boolean) :
            Event()

        data object EditUserRateSuccess : Event()
        data object EditUserRateError : Event()

        data class OnLoadMorePageError(val message: String, val error: Throwable) : Event()
        data class OnLoadPageError(val message: String, val error: Throwable) : Event()
        data class ItemRemovedLocally(val userRateId: Long) : Event()
        data class UpdateUserRateLocally(val userRate: UserRateUiModel) : Event()
    }

    sealed class Effect {
        data object Error : Effect()
        data object EditUserRateSuccess : Effect()
    }

    sealed class Command {
        data class LoadPage(val status: String, val page: Int, val isLoadMore: Boolean = false) :
            Command()

        data class UpdateUserRate(val userRate: EditRateUiModel, val statusId: String) : Command()
        data class IncrementUserRate(val userRateId: Long, val statusId: String) : Command()
    }
}

enum class PersonalListStatus {
    Initial,
    Loading,
    Empty,
    Error,
    Loaded,
}
