package com.dezdeqness.feature.details.person.presentation.store

import com.dezdeqness.domain.repository.PersonRepository
import com.dezdeqness.feature.details.common.presentation.store.BaseDetailsCommand
import com.dezdeqness.feature.details.common.presentation.store.BaseDetailsEvent
import com.dezdeqness.feature.details.person.presentation.composer.PersonDetailsComposer
import com.dezdeqness.foundation.Logger
import kotlinx.coroutines.flow.flow
import money.vivid.elmslie.core.store.Actor
import javax.inject.Inject

class PersonDetailsActor @Inject constructor(
    private val personRepository: PersonRepository,
    private val composer: PersonDetailsComposer,
    private val logger: Logger,
) : Actor<PersonDetailsNamespace.Command, PersonDetailsNamespace.Event>() {

    override fun execute(command: PersonDetailsNamespace.Command) =
        when (command) {
            is PersonDetailsNamespace.Command.Base -> when (val base = command.command) {
                is BaseDetailsCommand.LoadDetails -> flow {
                    try {
                        val entity = personRepository
                            .getPersonDetailsById(base.id)
                            .getOrThrow()
                        emit(
                            PersonDetailsNamespace.Event.OnDetailsLoaded(
                                title = entity.russian.ifEmpty { entity.name },
                                shareUrl = entity.url,
                                sections = composer.compose(entity),
                            )
                        )
                    } catch (error: Throwable) {
                        val message = "Error during loading person details, id: ${base.id}"
                        logger.logInfo(TAG, message, error)
                        emit(
                            PersonDetailsNamespace.Event.Base(
                                BaseDetailsEvent.OnDetailsLoadError(message, error),
                            )
                        )
                    }
                }
            }
        }

    companion object {
        private const val TAG = "PersonDetailsActor"
    }
}
