package com.dezdeqness.feature.details.character.presentation.store

import com.dezdeqness.contract.auth.SessionManager
import com.dezdeqness.contract.favourite.repository.FavouriteRepository
import com.dezdeqness.domain.repository.CharacterRepository
import com.dezdeqness.domain.usecases.FetchFavouritesUseCase
import com.dezdeqness.domain.usecases.ObserveFavouriteStatusUseCase
import com.dezdeqness.feature.details.character.presentation.composer.CharacterDetailsComposer
import com.dezdeqness.feature.details.common.presentation.store.BaseDetailsCommand
import com.dezdeqness.feature.details.common.presentation.store.BaseDetailsEvent
import com.dezdeqness.foundation.Logger
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import money.vivid.elmslie.core.store.Actor

class CharacterDetailsActor @Inject constructor(
    private val characterRepository: CharacterRepository,
    private val composer: CharacterDetailsComposer,
    private val observeFavouriteStatusUseCase: ObserveFavouriteStatusUseCase,
    private val fetchFavouritesUseCase: FetchFavouritesUseCase,
    private val favouriteRepository: FavouriteRepository,
    private val sessionManager: SessionManager,
    private val logger: Logger,
) : Actor<CharacterDetailsNamespace.Command, CharacterDetailsNamespace.Event>() {

    override fun execute(command: CharacterDetailsNamespace.Command) = when (command) {
        is CharacterDetailsNamespace.Command.Base -> mapBaseEvents(command.command)
    }

    private fun mapBaseEvents(command: BaseDetailsCommand): Flow<CharacterDetailsNamespace.Event> = when (command) {
        is BaseDetailsCommand.LoadDetails -> flow {
            try {
                val entity = characterRepository
                    .getCharacterDetailsById(command.id)
                    .getOrThrow()
                emit(
                    CharacterDetailsNamespace.Event.OnDetailsLoaded(
                        title = entity.russian.ifEmpty { entity.name },
                        shareUrl = entity.url,
                        sections = composer.compose(entity),
                        isAuthorized = sessionManager.isAuthorized,
                    ),
                )
            } catch (error: Throwable) {
                val message = "Error during loading character details, id: ${command.id}"
                logger.logInfo(TAG, message, error)
                emit(
                    CharacterDetailsNamespace.Event.Base(
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
            .map { CharacterDetailsNamespace.Event.Base(BaseDetailsEvent.FavouriteStatusChanged(it)) }

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
                            emit(CharacterDetailsNamespace.Event.Base(BaseDetailsEvent.FavouriteToggleSucceeded))
                        }
                        .onFailure {
                            logger.logInfo(TAG, "Failed to toggle favourite", it)
                            emit(CharacterDetailsNamespace.Event.Base(BaseDetailsEvent.FavouriteToggleFailed(it)))
                        }
                }
            }
        }
    }

    companion object {
        private const val TAG = "CharacterDetailsActor"
    }
}
