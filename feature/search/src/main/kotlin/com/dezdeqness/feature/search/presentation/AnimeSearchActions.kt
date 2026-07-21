package com.dezdeqness.feature.search.presentation

import com.dezdeqness.contract.filter.model.SearchSectionUiModel

interface AnimeSearchActions {
    fun onPullDownRefreshed()
    fun onLoadMore()
    fun onScrolled()
    fun onAnimeClicked(animeId: Long, title: String)
    fun onFabClicked()
    fun onQueryChanged(query: String)
    fun onFilterChanged(filtersList: List<SearchSectionUiModel> = listOf())
    fun onScrollInProgress(isScrollInProgress: Boolean)
    fun removeSearchHistoryItem(item: String)
}
