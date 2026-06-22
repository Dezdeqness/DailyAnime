package com.dezdeqness.feature.onboarding.flow.presentation.notifications

import androidx.lifecycle.viewModelScope
import com.dezdeqness.contract.settings.models.NotificationEnabledPreference
import com.dezdeqness.contract.settings.models.NotificationTimePreference
import com.dezdeqness.contract.settings.models.TimeEntity
import com.dezdeqness.contract.settings.repository.SettingsRepository
import com.dezdeqness.foundation.BaseViewModel
import com.dezdeqness.foundation.Logger
import com.dezdeqness.foundation.coroutines.CoroutineDispatcherProvider
import com.dezdeqness.shared.presentation.manager.WorkSchedulerManager
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn

class NotificationsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val workSchedulerManager: WorkSchedulerManager,
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    logger: Logger,
) : BaseViewModel(coroutineDispatcherProvider, logger) {

    override val viewModelTag = "NotificationsViewModel"

    private val events = Channel<Event>(Channel.BUFFERED)

    val uiState: StateFlow<NotificationsUiState> = flow {
        emitAll(
            events.receiveAsFlow().scan(loadInitialState()) { state, event ->
                when (event) {
                    is Event.EnabledChanged -> state.copy(enabled = event.enabled)
                    is Event.TimeChanged -> state.copy(time = event.time)
                }
            },
        )
    }
        .flowOn(coroutineDispatcherProvider.io())
        .distinctUntilChanged()
        .stateIn(viewModelScope, SharingStarted.Eagerly, NotificationsUiState())

    fun onToggled(enabled: Boolean) {
        events.trySend(Event.EnabledChanged(enabled))
    }

    fun onTimeChanged(hours: Int, minutes: Int) {
        events.trySend(Event.TimeChanged(TimeEntity(hours = hours, minutes = minutes)))
    }

    fun save() {
        val state = uiState.value
        launchOnIo {
            settingsRepository.setPreference(NotificationEnabledPreference, state.enabled)
            settingsRepository.setPreference(NotificationTimePreference, state.time)
            workSchedulerManager.scheduleDailyWork()
        }
    }

    private suspend fun loadInitialState(): NotificationsUiState = runCatching {
        NotificationsUiState(
            enabled = settingsRepository.getPreference(NotificationEnabledPreference),
            time = settingsRepository.getPreference(NotificationTimePreference),
        )
    }.getOrElse { throwable ->
        logInfo("Error while loading notification settings", throwable)
        NotificationsUiState()
    }

    private sealed interface Event {
        data class EnabledChanged(val enabled: Boolean) : Event
        data class TimeChanged(val time: TimeEntity) : Event
    }
}
