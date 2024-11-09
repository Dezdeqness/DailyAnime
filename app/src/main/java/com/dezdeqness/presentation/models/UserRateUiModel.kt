package com.dezdeqness.presentation.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserRateUiModel(
    val rateId: Long,
    val id: Long,
    val name: String,
    val kind: String,
    val score: String,
    val episodes: Int,
    val logoUrl: String,
    val overallEpisodes: Int,
) : AdapterItem(), Parcelable {

    override fun id() = (rateId + id).toString()

    val isAnimeInProgress: Boolean
        get() = episodes != 0 && overallEpisodes != 0
}
