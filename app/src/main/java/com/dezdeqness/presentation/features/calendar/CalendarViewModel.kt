package com.dezdeqness.presentation.features.calendar

import com.dezdeqness.core.AppLogger
import com.dezdeqness.core.BaseViewModel
import com.dezdeqness.core.CoroutineDispatcherProvider
import com.dezdeqness.domain.model.AnimeCalendarEntity
import com.dezdeqness.domain.repository.CalendarRepository
import com.dezdeqness.presentation.action.Action
import com.dezdeqness.presentation.action.ActionConsumer
import com.dezdeqness.presentation.event.Event
import com.dezdeqness.presentation.event.EventListener
import com.dezdeqness.presentation.event.ScrollToTop
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class CalendarViewModel @Inject constructor(
    private val calendarRepository: CalendarRepository,
    private val calendarComposer: CalendarComposer,
    private val actionConsumer: ActionConsumer,
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    appLogger: AppLogger,
) : BaseViewModel(
    coroutineDispatcherProvider = coroutineDispatcherProvider,
    appLogger = appLogger,
), BaseViewModel.InitialLoaded, BaseViewModel.Refreshable, EventListener {

    private val _calendarStateFlow: MutableStateFlow<CalendarState> =
        MutableStateFlow(CalendarState())
    val calendarStateFlow: StateFlow<CalendarState> get() = _calendarStateFlow

    private var calendarItems: List<AnimeCalendarEntity> = listOf()

    private var query = ""

    init {
        actionConsumer.attachListener(this)
        onInitialLoad(
            action = { calendarRepository.getCalendar() },
            onSuccess = { items ->
                calendarItems = items
                val uiItems = calendarComposer.compose(items, query = query)
                _calendarStateFlow.value = _calendarStateFlow.value.copy(
                    items = uiItems,
                )
            },
        )
    }

    override fun viewModelTag() = "CalendarViewModel"

    override fun onEventConsumed(event: Event) {
        val value = _calendarStateFlow.value
        _calendarStateFlow.value = value.copy(
            events = value.events.toMutableList() - event
        )
    }

    override fun setPullDownIndicatorVisible(isVisible: Boolean) {
        _calendarStateFlow.value = _calendarStateFlow.value.copy(
            isPullDownRefreshing = isVisible,
        )
    }

    override fun setLoadingIndicatorVisible(isVisible: Boolean) {
        // TODO
    }

    override fun onPullDownRefreshed() {
        onPullDownRefreshed(
            action = { calendarRepository.getCalendar() },
            onSuccess = { items ->
                calendarItems = items
                val uiItems = calendarComposer.compose(items = items, query = query)
                _calendarStateFlow.value = _calendarStateFlow.value.copy(
                    items = uiItems,
                )
            },
        )
    }

    override fun onCleared() {
        super.onCleared()
        actionConsumer.detachListener()
    }

    override fun onEventReceive(event: Event) {
        val events = _calendarStateFlow.value.events
        _calendarStateFlow.value = _calendarStateFlow.value.copy(
            events = events + event,
        )
    }

    fun onActionReceive(action: Action) {
        launchOnIo {
            actionConsumer.consume(action)
        }
    }

    fun onQueryChanged(query: String) {
        if (this.query == query) {
            return
        }

        this.query = query

        launchOnIo {
            val uiItems = calendarComposer.compose(items = calendarItems, query = query)
            _calendarStateFlow.value = _calendarStateFlow.value.copy(
                items = uiItems,
                events = _calendarStateFlow.value.events + ScrollToTop,
            )
        }
    }

}
