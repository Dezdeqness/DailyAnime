package com.dezdeqness.presentation.features.personallist.userrate

import com.dezdeqness.presentation.models.RibbonStatusUiModel

data class PersonalListUserRateState(
    val ribbon: List<RibbonStatusUiModel> = listOf(),
    val isPullDownRefreshing: Boolean = false,
    val isError: Boolean,
)
