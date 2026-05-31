package com.dezdeqness.feature.details.common.presentation.store

import com.dezdeqness.contract.favourite.model.FavouriteButtonState
import com.dezdeqness.feature.details.common.presentation.DetailsSection

interface DetailsState {
    val id: Long
    val status: DetailsStatus
    val title: String
    val shareUrl: String
    val sections: List<DetailsSection>
    val favouriteButton: FavouriteButtonState
}
