package com.dezdeqness.data.repository

import com.dezdeqness.data.provider.HistorySearchListProvider
import com.dezdeqness.domain.repository.HistorySearchRepository

class HistorySearchRepositoryImpl(
    private val historySearchProvider: HistorySearchListProvider,
) : HistorySearchRepository {
    override suspend fun addSearchHistory(history: String) {
        val trimmed = history.trim()
        if (trimmed.isEmpty()) return

        val current = historySearchProvider.getSearchHistory().toMutableList()
        current.remove(trimmed)

        current.add(0, trimmed)

        if (current.size > LIMIT) {
            current.removeLastOrNull()
        }

        historySearchProvider.setSearchHistory(current)
    }

    override suspend fun removeSearchHistory(history: String) {
        val current = historySearchProvider.getSearchHistory().toMutableList()
        current.remove(history)
        historySearchProvider.setSearchHistory(current)
    }

    override fun getSearchHistoryFlow() = historySearchProvider.getSearchHistoryFlow()

    override suspend fun getSearchHistory() = historySearchProvider.getSearchHistory()

    companion object {
        private const val LIMIT = 5
    }
}
