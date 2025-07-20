package com.dezdeqness.contract.history.repository

import com.dezdeqness.contract.history.model.HistoryEntity

interface HistoryRepository {
    fun getUserHistory(page: Int, limit: Int): Result<List<HistoryEntity>>
}
