package com.dezdeqness.domain.repository

import kotlinx.coroutines.flow.Flow

interface HistorySearchRepository {
    suspend fun addSearchHistory(history: String)
    suspend fun removeSearchHistory(history: String)
    fun getSearchHistoryFlow(): Flow<List<String>>
    suspend fun getSearchHistory(): List<String>
}
