package com.dezdeqness.feature.favourite.presentation

import androidx.lifecycle.viewModelScope
import com.dezdeqness.contract.favourite.repository.FavouriteRepository
import com.dezdeqness.foundation.BaseViewModel
import com.dezdeqness.foundation.Logger
import com.dezdeqness.foundation.coroutines.CoroutineDispatcherProvider
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
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
        flow {
            emit(FavouritesUiState(status = Status.Loading))

            val result = favouriteRepository.getFavourites(userId)

            result
                .onSuccess { favourites ->
                    val uiItems = favourites.map(favouriteMapper::map)

                    emit(
                        FavouritesUiState(
                            status = if (uiItems.isEmpty()) Status.Empty else Status.Loaded,
                            items = uiItems,
                        )
                    )
                }
                .onFailure { throwable ->
                    logInfo("Error fetching favourites", throwable)
                    emit(FavouritesUiState(status = Status.Error))
                }
        }
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
}
