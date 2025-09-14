package com.dezdeqness.feature.history.presentation.store

import com.dezdeqness.data.core.AppLogger
import com.dezdeqness.domain.usecases.GetHistoryUseCase
import com.dezdeqness.feature.history.presentation.HistoryComposer
import kotlinx.coroutines.flow.flow
import money.vivid.elmslie.core.store.Actor
import javax.inject.Inject

class HistoryActor @Inject constructor(
    private val getHistoryUseCase: GetHistoryUseCase,
    private val historyComposer: HistoryComposer,
    private val appLogger: AppLogger,
) : Actor<HistoryNamespace.Command, HistoryNamespace.Event>() {
    override fun execute(command: HistoryNamespace.Command) =
        when (command) {
            is HistoryNamespace.Command.LoadPage -> flow {
                try {
                    val result = getHistoryUseCase.invoke(command.page)
                    val state = result.getOrThrow()
                    val items = historyComposer.compose(state.list)
                    if (command.isLoadMore) {
                        emit(
                            HistoryNamespace.Event.OnLoadMorePageLoaded(
                                list = items,
                                hasNextPage = state.hasNextPage
                            )
                        )
                    } else {
                        emit(
                            HistoryNamespace.Event.OnPageLoaded(
                                list = items,
                                hasNextPage = state.hasNextPage,
                            )
                        )
                    }
                } catch (e: Throwable) {
                    val message: String
                    val event = if (command.isLoadMore) {
                        message = "Error during load more of history list, page: ${command.page}"
                        HistoryNamespace.Event.OnLoadMorePageError(message, e)
                    } else {
                        message = "Error during initial loading of state of history list"
                        HistoryNamespace.Event.OnLoadPageError(message, e)
                    }

                    appLogger.logInfo(TAG, message, e)
                    emit(event)
                }
            }
        }

    companion object {
        const val TAG = "HistoryActor"
    }
}
