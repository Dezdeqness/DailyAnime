package com.dezdeqness.presentation.features.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dezdeqness.domain.model.AnimeCalendarEntity
import com.dezdeqness.domain.repository.CalendarRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class CalendarViewModel @Inject constructor(
    private val calendarRepository: CalendarRepository,
    private val calendarComposer: CalendarComposer,
) : ViewModel() {

    private val _calendarStateFlow: MutableStateFlow<CalendarState> =
        MutableStateFlow(CalendarState())

    val calendarStateFlow: StateFlow<CalendarState> get() = _calendarStateFlow
    private var calendarItems: List<AnimeCalendarEntity> = listOf()

    private var query = ""

    init {
        fetchCalendar()
    }

    private fun fetchCalendar() {
        viewModelScope.launch(Dispatchers.IO) {
            calendarRepository
                .getCalendar()
                .onSuccess { items ->
                    calendarItems = items
                    val uiItems = calendarComposer.compose(items, query = query)
                    _calendarStateFlow.value = _calendarStateFlow.value.copy(
                        items = uiItems,
                        isPullDownRefreshing = false,
                        scrollToTop = false,
                    )
                }
                .onFailure {

                }
        }
    }

    fun onRefreshSwiped() {
        _calendarStateFlow.value = _calendarStateFlow.value.copy(
            isPullDownRefreshing = true,
            scrollToTop = false,
        )
        fetchCalendar()
    }

    fun onQueryChanged(query: String) {
        if (this.query == query) {
            return
        }

        this.query = query

        viewModelScope.launch(Dispatchers.IO) {
            val uiItems = calendarComposer.compose(calendarItems, query = query)
            _calendarStateFlow.value = _calendarStateFlow.value.copy(
                items = uiItems,
                scrollToTop = true,
            )
        }
    }

}
