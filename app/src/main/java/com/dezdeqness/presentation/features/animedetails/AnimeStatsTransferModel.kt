package com.dezdeqness.presentation.features.animedetails

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AnimeStatsTransferModel(
    val name: String,
    val value: Int,
) : Parcelable
