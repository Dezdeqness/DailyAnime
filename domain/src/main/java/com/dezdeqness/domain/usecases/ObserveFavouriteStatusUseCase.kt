package com.dezdeqness.domain.usecases

import com.dezdeqness.contract.favourite.model.FavouriteButtonState
import com.dezdeqness.contract.favourite.model.FavouriteLinkedType
import com.dezdeqness.contract.favourite.model.FavouriteType
import com.dezdeqness.contract.favourite.model.FavouritesCacheState
import com.dezdeqness.contract.favourite.model.matchingCacheTypes
import com.dezdeqness.contract.favourite.repository.FavouriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class ObserveFavouriteStatusUseCase(
    private val favouriteRepository: FavouriteRepository,
) {

    operator fun invoke(
        targetId: Long,
        type: FavouriteLinkedType,
        isAuthorized: Boolean,
    ): Flow<FavouriteButtonState> {
        val matchingTypes = type.matchingCacheTypes()
        return favouriteRepository.favourites
            .map { state -> resolve(state, targetId, matchingTypes, isAuthorized) }
            .distinctUntilChanged()
    }

    private fun resolve(
        state: FavouritesCacheState,
        targetId: Long,
        matchingTypes: Set<FavouriteType>,
        isAuthorized: Boolean,
    ): FavouriteButtonState {
        if (!isAuthorized) return FavouriteButtonState.Hidden
        return when (state) {
            FavouritesCacheState.Empty,
            FavouritesCacheState.Loading,
            is FavouritesCacheState.Error,
            -> FavouriteButtonState.Disabled

            is FavouritesCacheState.Loaded -> {
                val isFavourite = state.items.any { it.id == targetId && it.type in matchingTypes }
                FavouriteButtonState.Idle(isFavourite = isFavourite)
            }
        }
    }
}
