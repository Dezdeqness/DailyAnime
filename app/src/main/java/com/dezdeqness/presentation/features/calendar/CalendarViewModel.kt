package com.dezdeqness.presentation.features.calendar

import com.dezdeqness.core.AppLogger
import com.dezdeqness.core.BaseViewModel
import com.dezdeqness.core.CoroutineDispatcherProvider
import com.dezdeqness.domain.model.AnimeCalendarEntity
import com.dezdeqness.domain.repository.CalendarRepository
import com.dezdeqness.presentation.action.Action
import com.dezdeqness.presentation.action.ActionConsumer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
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
), BaseViewModel.InitialLoaded, BaseViewModel.Refreshable {

    private val _calendarStateFlow: MutableStateFlow<CalendarState> =
        MutableStateFlow(CalendarState())
    val calendarStateFlow: StateFlow<CalendarState> get() = _calendarStateFlow

    private var calendarItems: List<AnimeCalendarEntity> = listOf()

    private var query = ""

    init {
        actionConsumer.attachListener(this)
    }

    fun onInitialLoad() {
        onInitialLoad(
            action = { calendarRepository.getCalendar() },
            onSuccess = { items ->
                calendarItems = items
                val uiItems = calendarComposer.compose(items, query = query)
                _calendarStateFlow.value = _calendarStateFlow.value.copy(
                    list = uiItems,
                    isEmptyStateShowing = uiItems.isEmpty(),
                    isErrorStateShowing = false,
                )
            },
            onFailure = {
                _calendarStateFlow.value = _calendarStateFlow.value.copy(
                    isErrorStateShowing = true,
                )
                logInfo("Error during loading of initial of state of calendar list", it)
            }
        )
    }

    override val viewModelTag = "CalendarViewModel"

    override fun setPullDownIndicatorVisible(isVisible: Boolean) {
        _calendarStateFlow.value = _calendarStateFlow.value.copy(
            isPullDownRefreshing = isVisible,
        )
    }

    override fun setLoadingIndicatorVisible(isVisible: Boolean) {
        _calendarStateFlow.value = _calendarStateFlow.value.copy(
            isInitialLoadingIndicatorShowing = isVisible,
        )
    }

    override fun onPullDownRefreshed() {
        onPullDownRefreshed(
            action = { calendarRepository.getCalendar() },
            onSuccess = { items ->
                calendarItems = items
                val uiItems = calendarComposer.compose(items = items, query = query)
                _calendarStateFlow.value = _calendarStateFlow.value.copy(
                    list = uiItems,
                    isEmptyStateShowing = uiItems.isEmpty(),
                    isErrorStateShowing = false
                )
            },
            onFailure = {
                logInfo("Error during pull down of calendar list", it)
            }
        )
    }

    override fun onCleared() {
        super.onCleared()
        actionConsumer.detachListener()
    }

    fun onActionReceive(action: Action) {
        launchOnIo {
            actionConsumer.consume(action)
        }
    }
    fun onScrolled() {
        if (calendarStateFlow.value.isScrollNeed) {
            _calendarStateFlow.update {
                _calendarStateFlow.value.copy(isScrollNeed = false)
            }
        }
    }

    fun onQueryChanged(query: String) {
        if (this.query == query) {
            return
        }

        this.query = query

        if (_calendarStateFlow.value.isErrorStateShowing) {
            return
        }

        launchOnIo {
            val uiItems = calendarComposer.compose(items = calendarItems, query = query)
            _calendarStateFlow.value = _calendarStateFlow.value.copy(
                list = uiItems,
                isEmptyStateShowing = uiItems.isEmpty(),
                isScrollNeed = true,
            )
        }
    }

}
