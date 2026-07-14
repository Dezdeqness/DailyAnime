package com.dezdeqness.feature.home.presentation

interface HomeActions {
    fun onInitialLoad()
    fun onAnimeClicked(animeId: Long, title: String)
    fun onCalendarHeaderClicked()
    fun onHistoryHeaderClicked()
}
