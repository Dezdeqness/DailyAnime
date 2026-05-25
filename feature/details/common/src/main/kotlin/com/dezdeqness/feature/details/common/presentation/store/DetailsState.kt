package com.dezdeqness.feature.details.common.presentation.store

interface DetailsState {
    val id: Long
    val status: DetailsStatus
    val title: String
    val shareUrl: String
}
