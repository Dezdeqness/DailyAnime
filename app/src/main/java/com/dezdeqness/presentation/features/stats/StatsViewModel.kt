package com.dezdeqness.presentation.features.stats

import com.dezdeqness.core.AppLogger
import com.dezdeqness.core.BaseViewModel
import com.dezdeqness.core.CoroutineDispatcherProvider
import com.dezdeqness.domain.repository.AccountRepository
import com.dezdeqness.presentation.event.Event
import com.dezdeqness.presentation.features.history.HistoryState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class StatsViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    private val statsComposer: StatsComposer,
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    appLogger: AppLogger,
) : BaseViewModel(
    coroutineDispatcherProvider = coroutineDispatcherProvider,
    appLogger = appLogger,
) {

    private val _statsStateFlow: MutableStateFlow<StatsState> = MutableStateFlow(StatsState())
    val statsStateFlow: StateFlow<StatsState> get() = _statsStateFlow

    init {
        launchOnIo {
            accountRepository.getProfileLocal()?.let {
                val itemList = statsComposer.compose(it)
                launchOnMain {
                    _statsStateFlow.value = _statsStateFlow.value.copy(
                        items = itemList,
                    )
                }
            }
        }
    }

    override val viewModelTag = "StatsViewModel"

    override fun onEventConsumed(event: Event) {

    }

}
