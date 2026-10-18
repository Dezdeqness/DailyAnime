package com.dezdeqness.di.source.unsupported

import com.dezdeqness.contract.favourite.model.FavouriteKind
import com.dezdeqness.contract.favourite.model.FavouriteLinkedType
import com.dezdeqness.contract.favourite.model.FavouritesCacheState
import com.dezdeqness.contract.favourite.repository.FavouriteRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UnsupportedFavouriteRepository @Inject constructor() : FavouriteRepository {

    override val favourites: StateFlow<FavouritesCacheState> =
        MutableStateFlow<FavouritesCacheState>(FavouritesCacheState.Empty).asStateFlow()

    override suspend fun fetchFavourites(userId: Long, force: Boolean): Result<Unit> = unsupported()

    override suspend fun toggleFavourite(
        userId: Long,
        targetId: Long,
        type: FavouriteLinkedType,
        kind: FavouriteKind?,
    ): Result<Unit> = unsupported()

    override fun clearCache() = Unit
}
