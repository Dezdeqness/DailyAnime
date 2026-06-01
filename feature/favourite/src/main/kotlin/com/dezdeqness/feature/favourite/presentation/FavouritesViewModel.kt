package com.dezdeqness.feature.favourite.presentation

import androidx.lifecycle.viewModelScope
import com.dezdeqness.contract.favourite.model.FavouritesCacheState
import com.dezdeqness.contract.favourite.repository.FavouriteRepository
import com.dezdeqness.foundation.BaseViewModel
import com.dezdeqness.foundation.Logger
import com.dezdeqness.foundation.coroutines.CoroutineDispatcherProvider
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Named

class FavouritesViewModel @Inject constructor(
    @Named("userId") private val userId: Long,
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    logger: Logger,
    private val favouriteRepository: FavouriteRepository,
    private val favouriteMapper: FavouriteMapper,
) : BaseViewModel(coroutineDispatcherProvider, logger) {

    override val viewModelTag = "FavouritesViewModel"

    val favouritesState: StateFlow<FavouritesUiState> =
        favouriteRepository.favourites
            .onStart {
                launchOnIo {
                    favouriteRepository.fetchFavourites(userId)
                        .onFailure { logInfo("Error fetching favourites", it) }
                }
            }
            .map(::toUiState)
            .catch { throwable ->
                logInfo("Error in favourites flow", throwable)
                emit(FavouritesUiState(status = Status.Error))
            }
            .flowOn(coroutineDispatcherProvider.io())
            .distinctUntilChanged()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = FavouritesUiState(status = Status.Initial)
            )

    private fun toUiState(cache: FavouritesCacheState): FavouritesUiState = when (cache) {
        FavouritesCacheState.Empty -> FavouritesUiState(status = Status.Initial)
        FavouritesCacheState.Loading -> FavouritesUiState(status = Status.Loading)
        is FavouritesCacheState.Error -> FavouritesUiState(status = Status.Error)
        is FavouritesCacheState.Loaded -> {
            val items = cache.items.map(favouriteMapper::map)
            FavouritesUiState(
                status = if (items.isEmpty()) Status.Empty else Status.Loaded,
                items = items,
            )
        }
    }
}
