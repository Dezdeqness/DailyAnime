package com.dezdeqness.feature.details.common.presentation.store

import com.dezdeqness.contract.favourite.model.FavouriteKind
import com.dezdeqness.contract.favourite.model.FavouriteLinkedType

sealed interface BaseDetailsCommand {
    data class LoadDetails(val id: Long) : BaseDetailsCommand

    data class ObserveFavouriteStatus(
        val targetId: Long,
        val type: FavouriteLinkedType,
    ) : BaseDetailsCommand

    data class FetchFavourites(val force: Boolean = false) : BaseDetailsCommand

    data class ToggleFavourite(
        val targetId: Long,
        val type: FavouriteLinkedType,
        val kind: FavouriteKind? = null,
    ) : BaseDetailsCommand
}
