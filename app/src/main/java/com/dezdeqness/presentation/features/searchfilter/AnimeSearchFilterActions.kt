package com.dezdeqness.presentation.features.searchfilter

interface AnimeSearchFilterActions {
    fun onDismissed()
    fun onCellClicked(innerId: String, cellId: String, isSelected: Boolean)
    fun onApplyFilter()
    fun onResetFilter()
}
