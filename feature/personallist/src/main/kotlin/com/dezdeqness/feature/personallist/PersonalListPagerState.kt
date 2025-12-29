package com.dezdeqness.feature.personallist

import androidx.compose.runtime.Immutable
import com.dezdeqness.domain.model.Sort
import com.dezdeqness.shared.presentation.model.RibbonStatusUiModel

@Immutable
data class PersonalListPagerState(
    val ribbon: List<RibbonStatusUiModel> = listOf(),
    val selectedRibbonId: String = "",
    val currentSortId: String = Sort.NAME.sort,
    val isInitialLoading: Boolean = false,
    val placeholder: Placeholder = Placeholder.None,
)

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
