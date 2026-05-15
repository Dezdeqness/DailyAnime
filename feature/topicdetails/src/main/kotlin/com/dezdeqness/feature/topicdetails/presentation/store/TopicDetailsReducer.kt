package com.dezdeqness.feature.topicdetails.presentation.store

import com.dezdeqness.feature.topicdetails.presentation.models.LinkedEntityState
import com.dezdeqness.shared.presentation.feature.topic.TopicPresentationComposer.Companion.LINKED_TYPE_ANIME
import com.dezdeqness.shared.presentation.feature.topic.TopicPresentationComposer.Companion.LINKED_TYPE_CHARACTER
import money.vivid.elmslie.core.store.StateReducer

private val SUPPORTED_LINKED_TYPES = setOf(LINKED_TYPE_ANIME, LINKED_TYPE_CHARACTER)

val topicDetailsReducer = object :
    StateReducer<TopicDetailsNamespace.Event, TopicDetailsNamespace.State, TopicDetailsNamespace.Effect, TopicDetailsNamespace.Command>() {
    override fun Result.reduce(event: TopicDetailsNamespace.Event) {
        when (event) {
            is TopicDetailsNamespace.Event.InitialLoad -> {
                state {
                    state.copy(
                        topicId = event.topicId,
                        topic = null,
                        linkedEntity = LinkedEntityState.Initial,
                        status = TopicDetailsStatus.Loading,
                        isPullDownRefreshing = false,
                    )
                }
                commands { +TopicDetailsNamespace.Command.LoadTopic(event.topicId) }
            }

            is TopicDetailsNamespace.Event.Refresh -> {
                state {
                    state.copy(
                        topicId = event.topicId,
                        linkedEntity = LinkedEntityState.Initial,
                        isPullDownRefreshing = true,
                    )
                }
                commands { +TopicDetailsNamespace.Command.LoadTopic(event.topicId) }
            }

            is TopicDetailsNamespace.Event.OnTopicLoaded -> {
                val linkedId = event.topic.linkedId
                val linkedType = event.topic.linkedType
                val shouldLoadLinked = linkedId != null && linkedType in SUPPORTED_LINKED_TYPES

                state {
                    state.copy(
                        topic = event.topic,
                        topicId = event.topic.topicId,
                        linkedEntity = if (shouldLoadLinked) LinkedEntityState.Loading else LinkedEntityState.Empty,
                        status = TopicDetailsStatus.Loaded,
                        isPullDownRefreshing = false,
                    )
                }
                if (shouldLoadLinked && linkedType != null) {
                    commands {
                        +TopicDetailsNamespace.Command.LoadLinkedEntity(
                            id = linkedId,
                            type = linkedType,
                        )
                    }
                }
            }

            is TopicDetailsNamespace.Event.OnTopicLoadError -> {
                state {
                    state.copy(
                        status = TopicDetailsStatus.Error,
                        isPullDownRefreshing = false,
                    )
                }
                effects { +TopicDetailsNamespace.Effect.Error }
            }

            is TopicDetailsNamespace.Event.OnLinkedEntityLoaded -> {
                state {
                    state.copy(
                        linkedEntity = LinkedEntityState.Loaded(event.entity),
                    )
                }
            }

            is TopicDetailsNamespace.Event.OnLinkedEntityLoadError -> {
                state {
                    state.copy(
                        linkedEntity = LinkedEntityState.Empty,
                    )
                }
            }
        }
    }
}
