package com.dezdeqness.feature.topicdetails.presentation.store

import com.dezdeqness.contract.anime.repository.AnimeRepository
import com.dezdeqness.contract.character.repository.CharacterRepository
import com.dezdeqness.contract.topic.repository.TopicRepository
import com.dezdeqness.feature.topicdetails.presentation.LinkedEntityComposer
import com.dezdeqness.foundation.Logger
import com.dezdeqness.shared.presentation.feature.topic.TopicPresentationComposer
import com.dezdeqness.shared.presentation.feature.topic.TopicPresentationComposer.Companion.LINKED_TYPE_ANIME
import com.dezdeqness.shared.presentation.feature.topic.TopicPresentationComposer.Companion.LINKED_TYPE_CHARACTER
import javax.inject.Inject
import kotlinx.coroutines.flow.flow
import money.vivid.elmslie.core.store.Actor

class TopicDetailsActor @Inject constructor(
    private val topicRepository: TopicRepository,
    private val animeRepository: AnimeRepository,
    private val characterRepository: CharacterRepository,
    private val linkedEntityComposer: LinkedEntityComposer,
    private val topicPresentationComposer: TopicPresentationComposer,
    private val logger: Logger,
) : Actor<TopicDetailsNamespace.Command, TopicDetailsNamespace.Event>() {

    override fun execute(command: TopicDetailsNamespace.Command) = when (command) {
        is TopicDetailsNamespace.Command.LoadTopic -> flow {
            try {
                val result = topicRepository.getTopicsById(command.topicId.toInt())
                result.onFailure { throw it }

                emit(
                    TopicDetailsNamespace.Event.OnTopicLoaded(
                        topic = topicPresentationComposer.compose(result.getOrThrow()),
                    ),
                )
            } catch (error: Throwable) {
                val message = "Error during loading topic details, id: ${command.topicId}"
                logger.logInfo(TAG, message, error)
                emit(TopicDetailsNamespace.Event.OnTopicLoadError(message, error))
            }
        }

        is TopicDetailsNamespace.Command.LoadLinkedEntity -> flow {
            try {
                val entity = when (command.type) {
                    LINKED_TYPE_ANIME -> {
                        val anime = animeRepository
                            .getDetails(id = command.id, isAuthorized = false)
                            .getOrThrow()
                        linkedEntityComposer.compose(anime)
                    }

                    LINKED_TYPE_CHARACTER -> {
                        val character = characterRepository
                            .getCharacterDetailsById(id = command.id)
                            .getOrThrow()
                        linkedEntityComposer.compose(character)
                    }

                    else -> error("Unsupported linked type: ${command.type}")
                }

                emit(TopicDetailsNamespace.Event.OnLinkedEntityLoaded(entity = entity))
            } catch (error: Throwable) {
                val message = "Error during loading linked entity, id: ${command.id}, type: ${command.type}"
                logger.logInfo(TAG, message, error)
                emit(TopicDetailsNamespace.Event.OnLinkedEntityLoadError(message, error))
            }
        }
    }

    companion object {
        private const val TAG = "TopicDetailsActor"
    }
}
