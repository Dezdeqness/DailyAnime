package com.dezdeqness.presentation.features.animelist

import com.dezdeqness.presentation.action.Action
import com.dezdeqness.presentation.models.AnimeSearchFilter

interface AnimeSearchActions {
    fun onPullDownRefreshed()
    fun onLoadMore()
    fun onScrolled()
    fun onInitialLoad()
    fun onActionReceived(action: Action)
    fun onFabClicked()
    fun onQueryChanged(query: String)
    fun onFilterChanged(filtersList: List<AnimeSearchFilter>)
}
