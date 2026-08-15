package com.dezdeqness.feature.calendar.presentation

import androidx.lifecycle.viewModelScope
import com.dezdeqness.contract.calendar.model.AnimeCalendarEntity
import com.dezdeqness.contract.calendar.repository.CalendarRepository
import com.dezdeqness.feature.calendar.presentation.models.CalendarListUiModel
import com.dezdeqness.foundation.BaseViewModel
import com.dezdeqness.foundation.Logger
import com.dezdeqness.foundation.coroutines.CoroutineDispatcherProvider
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class CalendarViewModel @Inject constructor(
    private val calendarRepository: CalendarRepository,
    private val calendarComposer: CalendarComposer,
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    logger: Logger,
) : BaseViewModel(coroutineDispatcherProvider, logger) {

    override val viewModelTag = "CalendarViewModel"

    private val loadEvents = MutableSharedFlow<LoadEvent>(extraBufferCapacity = 1)

    private val _pullRefreshFlow = MutableStateFlow(false)
    val pullRefreshFlow: StateFlow<Boolean> get() = _pullRefreshFlow

    private val _scrollNeedFlow = MutableStateFlow(false)
    val scrollNeedFlow: StateFlow<Boolean> get() = _scrollNeedFlow

    private var calendarItems: List<AnimeCalendarEntity> = listOf()

    private var query = ""

    @OptIn(ExperimentalCoroutinesApi::class)
    val calendarStateFlow: StateFlow<CalendarState> = loadEvents
        .onStart { emit(LoadEvent.Initial) }
        .flatMapLatest { event ->
            flow {
                val result = if (event is LoadEvent.QueryChanged && calendarItems.isNotEmpty()) {
                    Result.success(calendarItems)
                } else {
                    calendarRepository.getCalendar()
                }

                emit(
                    LoadResult(
                        event = event,
                        result = result,
                    ),
                )
            }.flowOn(coroutineDispatcherProvider.io())
        }
        .scan(CalendarState()) { previous, loadResult ->
            val event = loadResult.event
            val result = loadResult.result

            result.onSuccess { items ->
                calendarItems = items
                val uiItems = calendarComposer.compose(items = items, query = query)

                if (event is LoadEvent.QueryChanged) {
                    _scrollNeedFlow.update { true }
                }

                return@scan previous.copy(
                    list = uiItems,
                    status = statusOf(uiItems),
                )
            }

            result.onFailure { throwable ->
                logInfo("Error during loading of calendar list", throwable)

                val newStatus = if (event is LoadEvent.Initial) {
                    CalendarStatus.Error
                } else {
                    previous.status
                }

                return@scan previous.copy(status = newStatus)
            }

            previous
        }
        .onEach { _pullRefreshFlow.tryEmit(false) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = CalendarState(),
        )

    fun onPullDownRefreshed() {
        loadEvents.tryEmit(LoadEvent.Refresh)
        _pullRefreshFlow.tryEmit(true)
    }

    fun onScrolled() {
        if (_scrollNeedFlow.value) {
            _scrollNeedFlow.update { false }
        }
    }

    fun onQueryChanged(query: String) {
        if (this.query == query) {
            return
        }

        this.query = query
        loadEvents.tryEmit(LoadEvent.QueryChanged)
    }

    private fun statusOf(uiItems: List<CalendarListUiModel>) =
        if (uiItems.isEmpty()) CalendarStatus.Empty else CalendarStatus.Loaded

    private sealed interface LoadEvent {
        data object Initial : LoadEvent
        data object Refresh : LoadEvent
        data object QueryChanged : LoadEvent
    }

    private data class LoadResult(
        val event: LoadEvent,
        val result: Result<List<AnimeCalendarEntity>>,
    )
}
