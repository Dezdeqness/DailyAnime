package com.dezdeqness.feature.details.common.presentation.store

sealed interface BaseDetailsEffect {
    data object Error : BaseDetailsEffect
    data class Share(val url: String) : BaseDetailsEffect
    data object FavouriteActionFailed : BaseDetailsEffect
}
