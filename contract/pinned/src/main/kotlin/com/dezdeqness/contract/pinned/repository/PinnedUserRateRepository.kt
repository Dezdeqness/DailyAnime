package com.dezdeqness.contract.pinned.repository

import com.dezdeqness.contract.pinned.models.PinnedUserRateEntity

interface PinnedUserRateRepository {
    suspend fun getAllByGroupId(groupId: Long): List<PinnedUserRateEntity>
    suspend fun insert(pinnedUserRateLocal: PinnedUserRateEntity)
    suspend fun deleteByUserRateId(userRateId: Long)
    suspend fun isPinned(userRateId: Long): Int
}
