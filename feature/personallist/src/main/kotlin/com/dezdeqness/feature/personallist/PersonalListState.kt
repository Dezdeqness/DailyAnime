package com.dezdeqness.feature.personallist

import androidx.compose.runtime.Immutable
import com.dezdeqness.domain.model.Sort
import com.dezdeqness.feature.personallist.model.UserRateUiModel
import com.dezdeqness.shared.presentation.model.RibbonStatusUiModel

@Immutable
data class PersonalListState(
    val ribbon: List<RibbonStatusUiModel> = listOf(),
    val currentRibbonId: String = "",
    val items: List<UserRateUiModel> = listOf(),
    val currentSortId: String = Sort.NAME.sort,
    val isPullDownRefreshing: Boolean = false,
    private val isInitialLoadingIndicatorShowing: Boolean = false,
    val isScrollNeed: Boolean = false,
    val placeholder: Placeholder = Placeholder.None,
    val currentBottomSheet: BottomSheet = BottomSheet.None,
) {
    val isLoadingStateShowing
        get() =
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

sealed interface BottomSheet {
    data object None : BottomSheet
    data class EditRate(
        val userRateId: Long,
        val title: String,
    ) : BottomSheet
}