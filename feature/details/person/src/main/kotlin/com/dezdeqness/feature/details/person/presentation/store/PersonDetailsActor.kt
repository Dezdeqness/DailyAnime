package com.dezdeqness.feature.details.person.presentation.store

import com.dezdeqness.contract.auth.SessionManager
import com.dezdeqness.contract.favourite.repository.FavouriteRepository
import com.dezdeqness.domain.model.toPrimaryFavouriteKind
import com.dezdeqness.domain.repository.PersonRepository
import com.dezdeqness.domain.usecases.FetchFavouritesUseCase
import com.dezdeqness.domain.usecases.ObserveFavouriteStatusUseCase
import com.dezdeqness.feature.details.common.presentation.store.BaseDetailsCommand
import com.dezdeqness.feature.details.common.presentation.store.BaseDetailsEvent
import com.dezdeqness.feature.details.person.presentation.composer.PersonDetailsComposer
import com.dezdeqness.foundation.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import money.vivid.elmslie.core.store.Actor
import javax.inject.Inject

class PersonDetailsActor @Inject constructor(
    private val personRepository: PersonRepository,
    private val composer: PersonDetailsComposer,
    private val observeFavouriteStatusUseCase: ObserveFavouriteStatusUseCase,
    private val fetchFavouritesUseCase: FetchFavouritesUseCase,
    private val favouriteRepository: FavouriteRepository,
    private val sessionManager: SessionManager,
    private val logger: Logger,
) : Actor<PersonDetailsNamespace.Command, PersonDetailsNamespace.Event>() {

    override fun execute(command: PersonDetailsNamespace.Command) =
        when (command) {
            is PersonDetailsNamespace.Command.Base -> mapBaseEvents(command.command)
        }

    private fun mapBaseEvents(command: BaseDetailsCommand): Flow<PersonDetailsNamespace.Event> = when (command) {
        is BaseDetailsCommand.LoadDetails -> flow {
            try {
                val entity = personRepository
                    .getPersonDetailsById(command.id)
                    .getOrThrow()
                emit(
                    PersonDetailsNamespace.Event.OnDetailsLoaded(
                        title = entity.russian.ifEmpty { entity.name },
                        shareUrl = entity.url,
                        sections = composer.compose(entity),
                        favouriteKind = entity.roles.toPrimaryFavouriteKind(),
                        isAuthorized = sessionManager.isAuthorized,
                    ),
                )
            } catch (error: Throwable) {
                val message = "Error during loading person details, id: ${command.id}"
                logger.logInfo(TAG, message, error)
                emit(
                    PersonDetailsNamespace.Event.Base(
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
            .map { PersonDetailsNamespace.Event.Base(BaseDetailsEvent.FavouriteStatusChanged(it)) }

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
                            emit(PersonDetailsNamespace.Event.Base(BaseDetailsEvent.FavouriteToggleSucceeded))
                        }
                        .onFailure {
                            logger.logInfo(TAG, "Failed to toggle favourite", it)
                            emit(PersonDetailsNamespace.Event.Base(BaseDetailsEvent.FavouriteToggleFailed(it)))
                        }
                }
            }
        }
    }

    companion object {
        private const val TAG = "PersonDetailsActor"
    }
}
