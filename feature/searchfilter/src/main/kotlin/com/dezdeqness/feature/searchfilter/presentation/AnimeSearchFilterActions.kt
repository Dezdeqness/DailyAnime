package com.dezdeqness.feature.searchfilter.presentation

interface AnimeSearchFilterActions {
    fun onDismissed()
    fun onCellClicked(innerId: String, cellId: String, isSelected: Boolean)
    fun onApplyFilter()
    fun onResetFilter()
}
