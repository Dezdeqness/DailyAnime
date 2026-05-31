package com.dezdeqness.domain.usecases

import com.dezdeqness.contract.favourite.repository.FavouriteRepository

class FetchFavouritesUseCase(
    private val favouriteRepository: FavouriteRepository,
) {

    suspend operator fun invoke(userId: Long, force: Boolean = false): Result<Unit> =
        favouriteRepository.fetchFavourites(userId = userId, force = force)
}
