package com.dezdeqness.presentation.features.animelist

import com.dezdeqness.presentation.action.Action
import com.dezdeqness.presentation.models.SearchSectionUiModel

interface AnimeSearchActions {
    fun onPullDownRefreshed()
    fun onLoadMore()
    fun onScrolled()
    fun onActionReceived(action: Action)
    fun onFabClicked()
    fun onQueryChanged(query: String)
    fun onFilterChanged(filtersList: List<SearchSectionUiModel>)
    fun onScrollInProgress(isScrollInProgress: Boolean)
    fun removeSearchHistoryItem(item: String)
}
