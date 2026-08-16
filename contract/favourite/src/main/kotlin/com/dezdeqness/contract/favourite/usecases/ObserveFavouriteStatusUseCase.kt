package com.dezdeqness.contract.favourite.usecases

import com.dezdeqness.contract.favourite.model.FavouriteButtonState
import com.dezdeqness.contract.favourite.model.FavouriteLinkedType
import kotlinx.coroutines.flow.Flow

interface ObserveFavouriteStatusUseCase {

    operator fun invoke(
        targetId: Long,
        type: FavouriteLinkedType,
        isAuthorized: Boolean,
    ): Flow<FavouriteButtonState>
}
