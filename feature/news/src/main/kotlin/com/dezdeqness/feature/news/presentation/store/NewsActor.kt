package com.dezdeqness.feature.news.presentation.store

import com.dezdeqness.domain.usecases.GetTopicUseCase
import com.dezdeqness.feature.news.presentation.NewsComposer
import com.dezdeqness.foundation.Logger
import kotlinx.coroutines.flow.flow
import money.vivid.elmslie.core.store.Actor
import javax.inject.Inject

class NewsActor @Inject constructor(
    private val getTopicUseCase: GetTopicUseCase,
    private val newsComposer: NewsComposer,
    private val logger: Logger,
) : Actor<NewsNamespace.Command, NewsNamespace.Event>() {
    override fun execute(command: NewsNamespace.Command) =
        when (command) {
            is NewsNamespace.Command.LoadPage -> flow {
                try {
                    val result = getTopicUseCase(forumType = FORUM_TYPE, pageNumber = command.page)
                    result.onFailure { throw it }

                    val state = result.getOrThrow()
                    val items = newsComposer.compose(state.list)

                    if (command.isLoadMore) {
                        emit(
                            NewsNamespace.Event.OnLoadMorePageLoaded(
                                list = items,
                                hasNextPage = state.hasNextPage,
                            )
                        )
                    } else {
                        emit(
                            NewsNamespace.Event.OnPageLoaded(
                                list = items,
                                hasNextPage = state.hasNextPage,
                            )
                        )
                    }
                } catch (e: Throwable) {
                    val message: String
                    val event = if (command.isLoadMore) {
                        message = "Error during load more of news list, page: ${command.page}"
                        NewsNamespace.Event.OnLoadMorePageError(message, e)
                    } else {
                        message = "Error during initial loading of news list"
                        NewsNamespace.Event.OnLoadPageError(message, e)
                    }
                    logger.logInfo(TAG, message, e)
                    emit(event)
                }
            }
        }

    companion object {
        const val TAG = "NewsActor"
        private const val FORUM_TYPE = "news"
    }
}
