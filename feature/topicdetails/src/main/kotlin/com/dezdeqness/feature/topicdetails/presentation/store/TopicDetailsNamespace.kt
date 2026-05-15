package com.dezdeqness.feature.topicdetails.presentation.store

import com.dezdeqness.feature.topicdetails.presentation.models.LinkedEntityState
import com.dezdeqness.feature.topicdetails.presentation.models.LinkedEntityUiModel
import com.dezdeqness.shared.presentation.feature.topic.model.TopicPresentationModel

interface TopicDetailsNamespace {
    data class State(
        val topicId: Long = 0L,
        val topic: TopicPresentationModel? = null,
        val linkedEntity: LinkedEntityState = LinkedEntityState.Initial,
        val status: TopicDetailsStatus = TopicDetailsStatus.Initial,
        val isPullDownRefreshing: Boolean = false,
    )

    sealed class Event {
        data class InitialLoad(val topicId: Long) : Event()
        data class Refresh(val topicId: Long) : Event()
        data class OnTopicLoaded(val topic: TopicPresentationModel) : Event()
        data class OnTopicLoadError(val message: String, val error: Throwable) : Event()
        data class OnLinkedEntityLoaded(val entity: LinkedEntityUiModel) : Event()
        data class OnLinkedEntityLoadError(val message: String, val error: Throwable) : Event()
    }

    sealed class Effect {
        data object Error : Effect()
    }

    sealed class Command {
        data class LoadTopic(val topicId: Long) : Command()
        data class LoadLinkedEntity(val id: Long, val type: String) : Command()
    }
}

enum class TopicDetailsStatus {
    Initial,
    Loading,
    Error,
    Loaded,
}
