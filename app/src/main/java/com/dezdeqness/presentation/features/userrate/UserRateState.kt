package com.dezdeqness.presentation.features.userrate

data class UserRateState(
    val rateId: Long = -1,
    val title: String = "",
    val selectedStatus: String = "",
    val score: Long = 8,
    val episode: Long = 0,
    val isEditMode: Boolean = false,
    val isContentChanged: Boolean = false,
    val isSelectStatusDialogShowed: Boolean = false,
    val comment: String = "",
)
