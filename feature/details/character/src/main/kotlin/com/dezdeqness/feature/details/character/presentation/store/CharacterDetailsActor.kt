package com.dezdeqness.feature.details.character.presentation.store

import com.dezdeqness.domain.repository.CharacterRepository
import com.dezdeqness.feature.details.character.presentation.composer.CharacterDetailsComposer
import com.dezdeqness.feature.details.common.presentation.store.BaseDetailsCommand
import com.dezdeqness.feature.details.common.presentation.store.BaseDetailsEvent
import com.dezdeqness.foundation.Logger
import kotlinx.coroutines.flow.flow
import money.vivid.elmslie.core.store.Actor
import javax.inject.Inject

class CharacterDetailsActor @Inject constructor(
    private val characterRepository: CharacterRepository,
    private val composer: CharacterDetailsComposer,
    private val logger: Logger,
) : Actor<CharacterDetailsNamespace.Command, CharacterDetailsNamespace.Event>() {

    override fun execute(command: CharacterDetailsNamespace.Command) =
        when (command) {
            is CharacterDetailsNamespace.Command.Base -> when (val base = command.command) {
                is BaseDetailsCommand.LoadDetails -> flow {
                    try {
                        val entity = characterRepository
                            .getCharacterDetailsById(base.id)
                            .getOrThrow()
                        emit(
                            CharacterDetailsNamespace.Event.OnDetailsLoaded(
                                title = entity.russian.ifEmpty { entity.name },
                                shareUrl = entity.url,
                                sections = composer.compose(entity),
                            )
                        )
                    } catch (error: Throwable) {
                        val message = "Error during loading character details, id: ${base.id}"
                        logger.logInfo(TAG, message, error)
                        emit(
                            CharacterDetailsNamespace.Event.Base(
                                BaseDetailsEvent.OnDetailsLoadError(message, error),
                            )
                        )
                    }
                }
            }
        }

    companion object {
        private const val TAG = "CharacterDetailsActor"
    }
}
