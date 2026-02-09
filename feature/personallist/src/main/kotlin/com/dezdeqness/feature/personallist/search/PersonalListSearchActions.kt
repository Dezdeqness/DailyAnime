package com.dezdeqness.feature.personallist.search

interface PersonalListSearchActions {
    fun onTabSelect(tab: PersonalListTab)
    fun onAnimeClick(animeId: Long, title: String)
}
