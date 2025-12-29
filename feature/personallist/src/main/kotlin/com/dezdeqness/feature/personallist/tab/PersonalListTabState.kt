package com.dezdeqness.feature.personallist.tab

import androidx.compose.runtime.Immutable
import com.dezdeqness.feature.personallist.Placeholder
import com.dezdeqness.feature.personallist.model.UserRateUiModel

@Immutable
data class PersonalListTabState(
    val statusId: String = "",
    val items: List<UserRateUiModel> = listOf(),
    val isPullDownRefreshing: Boolean = false,
    private val isInitialLoadingIndicatorShowing: Boolean = false,
    val isScrollNeed: Boolean = false,
    val placeholder: Placeholder = Placeholder.None,
) {
    val isLoadingStateShowing
        get() = items.isEmpty() && placeholder == Placeholder.None && isInitialLoadingIndicatorShowing

    internal fun copyLoading(isVisible: Boolean) =
        copy(isInitialLoadingIndicatorShowing = isVisible)
}
