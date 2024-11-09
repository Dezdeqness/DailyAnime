package com.dezdeqness.presentation.features.personallist

import com.dezdeqness.domain.model.Sort
import com.dezdeqness.presentation.models.RibbonStatusUiModel
import com.dezdeqness.presentation.models.UserRateUiModel

data class PersonalListState(
    val ribbon: List<RibbonStatusUiModel> = listOf(),
    val currentRibbonId: String = "",
    val items: List<UserRateUiModel> = listOf(),
    val currentSortId: String = Sort.NAME.sort,
    val isPullDownRefreshing: Boolean = false,
    private val isInitialLoadingIndicatorShowing: Boolean = false,
    val isScrollNeed: Boolean = false,
    val placeholder: Placeholder = Placeholder.None,
) {
    val isLoadingStateShowing get() =
        items.isEmpty() && placeholder == Placeholder.None && isInitialLoadingIndicatorShowing
}

sealed class Placeholder {
    sealed class Ribbon : Placeholder() {
        data object Empty : Ribbon()
        data object Error : Ribbon()
    }

    sealed class UserRate : Placeholder() {
        data object Empty : UserRate()
        data object Error : UserRate()
    }

    data object None : Placeholder()
}
