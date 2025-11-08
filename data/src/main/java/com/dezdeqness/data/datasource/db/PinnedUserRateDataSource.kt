package com.dezdeqness.data.datasource.db

import com.dezdeqness.contract.pinned.models.PinnedUserRateEntity

interface PinnedUserRateDataSource {
    suspend fun getAllByGroupId(groupId: Long): List<PinnedUserRateEntity>
    suspend fun insert(pinnedUserRateLocal: PinnedUserRateEntity)
    suspend fun deleteByUserRateId(userRateId: Long)
    suspend fun isPinned(userRateId: Long): Boolean
}
