package com.dezdeqness.presentation.features.userrate

import com.dezdeqness.domain.model.UserRateStatusEntity

data class UserRateState(
    val rateId: Long = -1,
    val title: String = "",
    val selectedStatus: String = UserRateStatusEntity.NONE.status,
    val score: Long = 0,
    val episode: Long = 0,
    val isEditMode: Boolean = false,
    val isContentChanged: Boolean = false,
    val isSelectStatusDialogShowed: Boolean = false,
    val comment: String = "",
)
