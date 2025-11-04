package com.dezdeqness.feature.favourite.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dezdeqness.contract.favourite.repository.FavouriteRepository
import com.dezdeqness.core.coroutines.CoroutineDispatcherProvider
import com.dezdeqness.data.core.AppLogger
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Named

class FavouritesViewModel @Inject constructor(
    @Named("userId") private val userId: Long,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
    private val appLogger: AppLogger,
    private val favouriteRepository: FavouriteRepository,
    private val favouriteMapper: FavouriteMapper,
) : ViewModel() {

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
                    appLogger.logInfo(TAG, throwable = throwable)
                    FavouritesUiState(status = Status.Error)
                }
        }
            .catch { throwable ->
                appLogger.logInfo(TAG, throwable = throwable)
                FavouritesUiState(status = Status.Error)
            }
            .flowOn(coroutineDispatcherProvider.io())
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = FavouritesUiState(status = Status.Loading)
            )

    companion object {
        private const val TAG = "FavouritesViewModel"
    }
}
