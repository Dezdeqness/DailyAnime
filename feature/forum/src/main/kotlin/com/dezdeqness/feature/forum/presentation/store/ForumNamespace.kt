package com.dezdeqness.feature.forum.presentation.store

import com.dezdeqness.feature.forum.presentation.models.ForumUiModel
import com.dezdeqness.shared.presentation.feature.topic.model.TopicPresentationModel

interface ForumNamespace {
    data class State(
        val list: List<ForumUiModel> = listOf(),
        val status: ForumStatus = ForumStatus.Initial,
        val isPullDownRefreshing: Boolean = false,
        val hotTopics: List<TopicPresentationModel> = listOf(),
        val hotTopicsStatus: HotTopicsStatus = HotTopicsStatus.Initial,
    )

    sealed class Event {
        data object InitialLoad : Event()
        data object Refresh : Event()
        data class OnLoaded(val list: List<ForumUiModel>) : Event()
        data class OnError(val message: String, val error: Throwable) : Event()
        data class OnHotTopicsLoaded(val topics: List<TopicPresentationModel>) : Event()
        data class OnHotTopicsError(val message: String, val error: Throwable) : Event()
    }

    sealed class Effect {
        data object Error : Effect()
    }

    sealed class Command {
        data object Load : Command()
        data object LoadHotTopics : Command()
    }
}

enum class ForumStatus {
    Initial,
    Loading,
    Empty,
    Error,
    Loaded,
}

enum class HotTopicsStatus {
    Initial,
    Loading,
    Error,
    Loaded,
}
