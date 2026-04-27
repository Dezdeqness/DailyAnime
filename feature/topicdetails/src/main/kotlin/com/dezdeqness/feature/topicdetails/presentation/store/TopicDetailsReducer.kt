package com.dezdeqness.feature.topicdetails.presentation.store

import com.dezdeqness.feature.topicdetails.presentation.models.BaseRelatedAnime
import money.vivid.elmslie.core.store.StateReducer

val topicDetailsReducer = object :
    StateReducer<TopicDetailsNamespace.Event, TopicDetailsNamespace.State, TopicDetailsNamespace.Effect, TopicDetailsNamespace.Command>() {
    override fun Result.reduce(event: TopicDetailsNamespace.Event) {
        when (event) {
            is TopicDetailsNamespace.Event.InitialLoad -> {
                state {
                    state.copy(
                        topicId = event.topicId,
                        topic = null,
                        relatedAnime = BaseRelatedAnime.Initial,
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
                        relatedAnime = BaseRelatedAnime.Initial,
                        isPullDownRefreshing = true,
                    )
                }
                commands { +TopicDetailsNamespace.Command.LoadTopic(event.topicId) }
            }

            is TopicDetailsNamespace.Event.OnTopicLoaded -> {
                val linkedId = event.topic.linkedId
                state {
                    state.copy(
                        topic = event.topic,
                        topicId = event.topic.topicId,
                        relatedAnime = if (linkedId != null) BaseRelatedAnime.Loading else BaseRelatedAnime.Empty,
                        status = TopicDetailsStatus.Loaded,
                        isPullDownRefreshing = false,
                    )
                }
                if (linkedId != null) {
                    commands { +TopicDetailsNamespace.Command.LoadRelatedAnime(linkedId) }
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

            is TopicDetailsNamespace.Event.OnRelatedAnimeLoaded -> {
                state {
                    state.copy(
                        relatedAnime = BaseRelatedAnime.Loaded(event.anime),
                    )
                }
            }

            is TopicDetailsNamespace.Event.OnRelatedAnimeLoadError -> {
                state {
                    state.copy(
                        relatedAnime = BaseRelatedAnime.Empty,
                    )
                }
            }
        }
    }
}
