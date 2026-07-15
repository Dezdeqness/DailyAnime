package com.dezdeqness.feature.calendar.presentation

interface CalendarActions {
    fun onPullDownRefreshed()
    fun onScrolled()
    fun onAnimeClicked(animeId: Long, title: String)
    fun onQueryChanged(query: String)
}
