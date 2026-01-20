package com.dezdeqness.feature.personallist

import androidx.compose.runtime.Immutable
import com.dezdeqness.shared.presentation.model.RibbonStatusUiModel

@Immutable
data class PersonalTabsListPagerState(
    val ribbon: List<RibbonStatusUiModel> = listOf(),
    val selectedRibbonId: String = "",
    val status: DataStatus,
) {

    companion object {
        fun PersonalTabsListPagerState.loading() = PersonalTabsListPagerState(
            status = DataStatus.Loading
        )

        fun loading() = PersonalTabsListPagerState(
            status = DataStatus.Loading
        )

        fun PersonalTabsListPagerState.empty() = this.copy(
            status = DataStatus.Empty,
            ribbon = emptyList(),
            selectedRibbonId = "",
        )

        fun PersonalTabsListPagerState.loaded(
            ribbon: List<RibbonStatusUiModel> = listOf(),
            selectedRibbonId: String = "",
        ) = this.copy(
            status = DataStatus.Loaded,
            ribbon = ribbon,
            selectedRibbonId = selectedRibbonId,
        )

        fun error() = PersonalTabsListPagerState(
            status = DataStatus.Error
        )
    }
}

enum class DataStatus {
    Loaded,
    Empty,
    Error,
    Loading,
}

sealed interface BottomSheet {
    data object None : BottomSheet
    data class EditRate(
        val userRateId: Long,
        val title: String,
    ) : BottomSheet
}
