package com.dezdeqness.presentation.features.history.store

import com.dezdeqness.domain.usecases.GetHistoryUseCase
import com.dezdeqness.data.core.AppLogger
import com.dezdeqness.presentation.features.history.HistoryComposer
import com.dezdeqness.presentation.features.history.store.HistoryNamespace.Command
import com.dezdeqness.presentation.features.history.store.HistoryNamespace.Event
import kotlinx.coroutines.flow.flow
import money.vivid.elmslie.core.store.Actor
import javax.inject.Inject

class HistoryActor @Inject constructor(
    private val getHistoryUseCase: GetHistoryUseCase,
    private val historyComposer: HistoryComposer,
    private val appLogger: AppLogger,
) : Actor<Command, Event>() {
    override fun execute(command: Command) =
        when (command) {
            is Command.LoadPage -> flow {
                try {
                    val result = getHistoryUseCase.invoke(command.page)
                    val state = result.getOrThrow()
                    val items = historyComposer.compose(state.list)
                    if (command.isLoadMore) {
                        emit(
                            Event.OnLoadMorePageLoaded(
                                list = items,
                                hasNextPage = state.hasNextPage
                            )
                        )
                    } else {
                        emit(
                            Event.OnPageLoaded(
                                list = items,
                                hasNextPage = state.hasNextPage,
                            )
                        )
                    }
                } catch (e: Exception) {
                    val message: String
                    val event = if (command.isLoadMore) {
                        message = "Error during load more of history list, page: ${command.page}"
                        Event.OnLoadMorePageError(message, e)
                    } else {
                        message = "Error during initial loading of state of history list"
                        Event.OnLoadPageError(message, e)
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
