package com.dezdeqness.feature.forum.presentation.store

import com.dezdeqness.domain.usecases.GetForumsUseCase
import com.dezdeqness.domain.usecases.GetHotTopicsUseCase
import com.dezdeqness.feature.forum.presentation.ForumComposer
import com.dezdeqness.foundation.Logger
import com.dezdeqness.shared.presentation.feature.topic.TopicPresentationComposer
import kotlinx.coroutines.flow.flow
import money.vivid.elmslie.core.store.Actor
import javax.inject.Inject

class ForumActor @Inject constructor(
    private val getForumsUseCase: GetForumsUseCase,
    private val getHotTopicsUseCase: GetHotTopicsUseCase,
    private val forumComposer: ForumComposer,
    private val topicPresentationComposer: TopicPresentationComposer,
    private val logger: Logger,
) : Actor<ForumNamespace.Command, ForumNamespace.Event>() {
    override fun execute(command: ForumNamespace.Command) =
        when (command) {
            is ForumNamespace.Command.Load -> flow {
                try {
                    val result = getForumsUseCase()
                    result.onFailure { throw it }

                    val items = forumComposer.compose(result.getOrThrow())

                    emit(ForumNamespace.Event.OnLoaded(list = items))
                } catch (e: Throwable) {
                    val message = "Error during loading of forum sections"
                    logger.logInfo(TAG, message, e)
                    emit(ForumNamespace.Event.OnError(message, e))
                }
            }

            is ForumNamespace.Command.LoadHotTopics -> flow {
                try {
                    val result = getHotTopicsUseCase()
                    result.onFailure { throw it }

                    val topics = topicPresentationComposer.compose(result.getOrThrow())

                    emit(ForumNamespace.Event.OnHotTopicsLoaded(topics = topics))
                } catch (e: Throwable) {
                    val message = "Error during loading of hot topics"
                    logger.logInfo(TAG, message, e)
                    emit(ForumNamespace.Event.OnHotTopicsError(message, e))
                }
            }
        }

    companion object {
        const val TAG = "ForumActor"
    }
}
