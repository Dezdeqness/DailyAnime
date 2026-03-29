package com.dezdeqness.feature.topicdetails.presentation.store

import com.dezdeqness.contract.anime.repository.AnimeRepository
import com.dezdeqness.contract.topic.repository.TopicRepository
import com.dezdeqness.feature.topicdetails.presentation.LinkedAnimeComposer
import com.dezdeqness.foundation.Logger
import com.dezdeqness.shared.presentation.feature.topic.TopicPresentationComposer
import kotlinx.coroutines.flow.flow
import money.vivid.elmslie.core.store.Actor
import javax.inject.Inject

class TopicDetailsActor @Inject constructor(
    private val topicRepository: TopicRepository,
    private val animeRepository: AnimeRepository,
    private val linkedAnimeComposer: LinkedAnimeComposer,
    private val topicPresentationComposer: TopicPresentationComposer,
    private val logger: Logger,
) : Actor<TopicDetailsNamespace.Command, TopicDetailsNamespace.Event>() {

    override fun execute(command: TopicDetailsNamespace.Command) =
        when (command) {
            is TopicDetailsNamespace.Command.LoadTopic -> flow {
                try {
                    val result = topicRepository.getTopicsById(command.topicId.toInt())
                    result.onFailure { throw it }

                    emit(
                        TopicDetailsNamespace.Event.OnTopicLoaded(
                            topic = topicPresentationComposer.compose(result.getOrThrow()),
                        )
                    )
                } catch (error: Throwable) {
                    val message = "Error during loading topic details, id: ${command.topicId}"
                    logger.logInfo(TAG, message, error)
                    emit(TopicDetailsNamespace.Event.OnTopicLoadError(message, error))
                }
            }

            is TopicDetailsNamespace.Command.LoadRelatedAnime -> flow {
                try {
                    val result = animeRepository.getDetails(id = command.animeId, isAuthorized = false)
                    result.onFailure { throw it }

                    emit(
                        TopicDetailsNamespace.Event.OnRelatedAnimeLoaded(
                            anime = linkedAnimeComposer.compose(result.getOrThrow()),
                        )
                    )
                } catch (error: Throwable) {
                    val message = "Error during loading related anime, id: ${command.animeId}"
                    logger.logInfo(TAG, message, error)
                    emit(TopicDetailsNamespace.Event.OnRelatedAnimeLoadError(message, error))
                }
            }
        }

    companion object {
        private const val TAG = "TopicDetailsActor"
    }
}
