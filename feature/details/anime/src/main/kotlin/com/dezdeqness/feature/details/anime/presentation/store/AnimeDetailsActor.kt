package com.dezdeqness.feature.details.anime.presentation.store

import com.dezdeqness.contract.auth.SessionManager
import com.dezdeqness.contract.auth.repository.AuthRepository
import com.dezdeqness.contract.favourite.repository.FavouriteRepository
import com.dezdeqness.domain.usecases.CreateOrUpdateUserRateUseCase
import com.dezdeqness.domain.usecases.FetchFavouritesUseCase
import com.dezdeqness.domain.usecases.GetAnimeDetailsUseCase
import com.dezdeqness.domain.usecases.ObserveFavouriteStatusUseCase
import com.dezdeqness.feature.details.anime.presentation.composer.AnimeDetailsComposer
import com.dezdeqness.feature.details.common.presentation.store.BaseDetailsCommand
import com.dezdeqness.feature.details.common.presentation.store.BaseDetailsEvent
import com.dezdeqness.foundation.Logger
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import money.vivid.elmslie.core.store.Actor

class AnimeDetailsActor @Inject constructor(
    private val getAnimeDetailsUseCase: GetAnimeDetailsUseCase,
    private val createOrUpdateUserRateUseCase: CreateOrUpdateUserRateUseCase,
    private val authRepository: AuthRepository,
    private val composer: AnimeDetailsComposer,
    private val observeFavouriteStatusUseCase: ObserveFavouriteStatusUseCase,
    private val fetchFavouritesUseCase: FetchFavouritesUseCase,
    private val favouriteRepository: FavouriteRepository,
    private val sessionManager: SessionManager,
    private val logger: Logger,
) : Actor<AnimeDetailsNamespace.Command, AnimeDetailsNamespace.Event>() {

    override fun execute(command: AnimeDetailsNamespace.Command) = when (command) {
        is AnimeDetailsNamespace.Command.Base -> mapBaseEvents(command.command)

        is AnimeDetailsNamespace.Command.CreateOrUpdateUserRate -> flow {
            try {
                val isCreate = !command.model.isUserRateExist
                val rate = createOrUpdateUserRateUseCase.invoke(
                    rateId = command.model.rateId,
                    targetId = command.animeId.toString(),
                    status = command.model.status,
                    episodes = command.model.episodes,
                    score = command.model.score,
                    comment = command.model.comment,
                ).getOrThrow()
                emit(AnimeDetailsNamespace.Event.OnUserRateSaved(isCreate = isCreate, rate = rate))
            } catch (error: Throwable) {
                logger.logInfo(TAG, "Error during edit user rate", error)
                emit(AnimeDetailsNamespace.Event.OnUserRateSaveError)
            }
        }
    }

    private fun mapBaseEvents(command: BaseDetailsCommand): Flow<AnimeDetailsNamespace.Event> = when (command) {
        is BaseDetailsCommand.LoadDetails -> flow {
            try {
                val result = getAnimeDetailsUseCase.invoke(command.id)
                val details = result.getOrThrow()
                val isAuthorized = authRepository.isAuthorized()
                emit(
                    AnimeDetailsNamespace.Event.OnDetailsLoaded(
                        details = details,
                        sections = composer.compose(details),
                        isAuthorized = isAuthorized,
                    ),
                )
            } catch (error: Throwable) {
                val message = "Error during loading anime details, id: ${command.id}"
                logger.logInfo(TAG, message, error)
                emit(
                    AnimeDetailsNamespace.Event.Base(
                        BaseDetailsEvent.OnDetailsLoadError(message, error),
                    ),
                )
            }
        }

        is BaseDetailsCommand.ObserveFavouriteStatus -> observeFavouriteStatusUseCase(
            targetId = command.targetId,
            type = command.type,
            isAuthorized = sessionManager.isAuthorized,
        )
            .map { AnimeDetailsNamespace.Event.Base(BaseDetailsEvent.FavouriteStatusChanged(it)) }

        is BaseDetailsCommand.FetchFavourites -> {
            val userId = sessionManager.currentSession?.userId
            if (userId == null) {
                emptyFlow()
            } else {
                flow {
                    fetchFavouritesUseCase(userId = userId, force = command.force)
                        .onFailure { logger.logInfo(TAG, "Failed to load favourites", it) }
                }
            }
        }

        is BaseDetailsCommand.ToggleFavourite -> {
            val userId = sessionManager.currentSession?.userId
            if (userId == null) {
                emptyFlow()
            } else {
                flow {
                    favouriteRepository.toggleFavourite(
                        userId = userId,
                        targetId = command.targetId,
                        type = command.type,
                        kind = command.kind,
                    )
                        .onSuccess {
                            emit(AnimeDetailsNamespace.Event.Base(BaseDetailsEvent.FavouriteToggleSucceeded))
                        }
                        .onFailure {
                            logger.logInfo(TAG, "Failed to toggle favourite", it)
                            emit(AnimeDetailsNamespace.Event.Base(BaseDetailsEvent.FavouriteToggleFailed(it)))
                        }
                }
            }
        }
    }

    companion object {
        private const val TAG = "AnimeDetailsActor"
    }
}
