package com.dezdeqness.presentation.features.userrate

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EditRateUiModel(
    val rateId: Long,
    val status: String,
    val episodes: Long,
    val score: Float,
    val comment: String,
) : Parcelable {
    val isUserRateExist get() = rateId != -1L
}
