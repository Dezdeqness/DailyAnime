package com.dezdeqness.feature.favourite.data

import com.dezdeqness.contract.favourite.model.FavouriteKind
import com.dezdeqness.contract.favourite.model.FavouriteLinkedType
import com.dezdeqness.contract.favourite.model.FavouritesCacheState
import com.dezdeqness.contract.favourite.model.matchingCacheTypes
import com.dezdeqness.contract.favourite.repository.FavouriteRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

@Singleton
internal class FavouriteRepositoryImpl @Inject constructor(
    private val remoteDataSource: FavouriteRemoteDataSource,
) : FavouriteRepository {

    private val _favourites = MutableStateFlow<FavouritesCacheState>(FavouritesCacheState.Empty)
    override val favourites: StateFlow<FavouritesCacheState> = _favourites.asStateFlow()

    private val loadMutex = Mutex()

    override suspend fun fetchFavourites(userId: Long, force: Boolean): Result<Unit> = loadMutex.withLock {
        if (!force && !needsLoad()) return Result.success(Unit)
        loadFromRemote(userId).map { }
    }

    override suspend fun toggleFavourite(
        userId: Long,
        targetId: Long,
        type: FavouriteLinkedType,
        kind: FavouriteKind?,
    ): Result<Unit> = if (isFavourite(targetId, type)) {
        remoteDataSource.removeFromFavourites(targetId = targetId, type = type)
            .onSuccess { removeLocally(targetId, type) }
            .map { }
    } else {
        remoteDataSource.addToFavourites(targetId = targetId, type = type, kind = kind)
            .onSuccess { loadFromRemote(userId) }
            .map { }
    }

    override fun clearCache() {
        _favourites.value = FavouritesCacheState.Empty
    }

    private suspend fun loadFromRemote(userId: Long): Result<Unit> {
        _favourites.value = FavouritesCacheState.Loading
        val result = remoteDataSource.getFavourites(userId = userId)
        result
            .onSuccess { items ->
                _favourites.value = FavouritesCacheState.Loaded(
                    items = items,
                    loadedAtMillis = System.currentTimeMillis(),
                )
            }
            .onFailure { _favourites.value = FavouritesCacheState.Error(it) }
        return result.map { }
    }

    private fun isFavourite(targetId: Long, type: FavouriteLinkedType): Boolean {
        val loaded = _favourites.value as? FavouritesCacheState.Loaded ?: return false
        val matchingTypes = type.matchingCacheTypes()
        return loaded.items.any { it.id == targetId && it.type in matchingTypes }
    }

    private fun removeLocally(targetId: Long, type: FavouriteLinkedType) {
        val loaded = _favourites.value as? FavouritesCacheState.Loaded ?: return
        val matchingTypes = type.matchingCacheTypes()
        val filtered = loaded.items.filterNot { it.id == targetId && it.type in matchingTypes }
        if (filtered.size == loaded.items.size) return
        _favourites.value = loaded.copy(items = filtered)
    }

    private fun needsLoad(): Boolean = when (val current = _favourites.value) {
        FavouritesCacheState.Empty,
        is FavouritesCacheState.Error,
        -> true

        is FavouritesCacheState.Loaded ->
            System.currentTimeMillis() - current.loadedAtMillis >= TTL_MILLIS

        FavouritesCacheState.Loading -> false
    }

    companion object {
        private const val TTL_MILLIS = 5 * 60 * 1000L
    }
}
