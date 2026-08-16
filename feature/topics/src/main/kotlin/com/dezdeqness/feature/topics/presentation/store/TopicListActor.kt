package com.dezdeqness.feature.topics.presentation.store

import com.dezdeqness.contract.topic.usecases.GetTopicUseCase
import com.dezdeqness.feature.topics.presentation.TopicListComposer
import com.dezdeqness.foundation.Logger
import javax.inject.Inject
import javax.inject.Named
import kotlinx.coroutines.flow.flow
import money.vivid.elmslie.core.store.Actor

class TopicListActor @Inject constructor(
    private val getTopicUseCase: GetTopicUseCase,
    private val topicListComposer: TopicListComposer,
    private val logger: Logger,
    @Named("forumType") private val forumType: String,
) : Actor<TopicListNamespace.Command, TopicListNamespace.Event>() {
    override fun execute(command: TopicListNamespace.Command) = when (command) {
        is TopicListNamespace.Command.LoadPage -> flow {
            try {
                val result = getTopicUseCase(forumType = forumType, pageNumber = command.page)
                result.onFailure { throw it }

                val state = result.getOrThrow()
                val items = topicListComposer.compose(state.list)

                if (command.isLoadMore) {
                    emit(
                        TopicListNamespace.Event.OnLoadMorePageLoaded(
                            list = items,
                            hasNextPage = state.hasNextPage,
                        ),
                    )
                } else {
                    emit(
                        TopicListNamespace.Event.OnPageLoaded(
                            list = items,
                            hasNextPage = state.hasNextPage,
                        ),
                    )
                }
            } catch (e: Throwable) {
                val message: String
                val event = if (command.isLoadMore) {
                    message = "Error during load more of topic list, page: ${command.page}"
                    TopicListNamespace.Event.OnLoadMorePageError(message, e)
                } else {
                    message = "Error during initial loading of topic list"
                    TopicListNamespace.Event.OnLoadPageError(message, e)
                }
                logger.logInfo(TAG, message, e)
                emit(event)
            }
        }
    }

    companion object {
        const val TAG = "TopicListActor"
    }
}
