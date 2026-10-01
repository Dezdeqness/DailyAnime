package com.dezdeqness.feature.search.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dezdeqness.contract.history.repository.HistorySearchRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SearchHistoryViewModel @Inject constructor(
    private val historySearchRepository: HistorySearchRepository,
) : ViewModel() {

    val history: StateFlow<List<String>> = historySearchRepository
        .getSearchHistoryFlow()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun onQuerySubmitted(query: String) {
        viewModelScope.launch { historySearchRepository.addSearchHistory(query) }
    }

    fun onRemoveClicked(item: String) {
        viewModelScope.launch { historySearchRepository.removeSearchHistory(item) }
    }
}
