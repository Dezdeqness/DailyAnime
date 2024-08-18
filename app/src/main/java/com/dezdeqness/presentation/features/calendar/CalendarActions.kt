package com.dezdeqness.presentation.features.calendar

import com.dezdeqness.presentation.action.Action

interface CalendarActions {
    fun onInitialLoad()
    fun onPullDownRefreshed()
    fun onScrolled()
    fun onActionReceived(action: Action)
    fun onQueryChanged(query: String)
}
