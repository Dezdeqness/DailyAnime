package com.dezdeqness.presentation.features.stats

import com.dezdeqness.data.core.AppLogger
import com.dezdeqness.core.BaseViewModel
import com.dezdeqness.core.coroutines.CoroutineDispatcherProvider
import com.dezdeqness.core.page.StatsState
import com.dezdeqness.domain.repository.AccountRepository
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

}
