package com.dezdeqness.data.repository

import com.dezdeqness.contract.pinned.models.PinnedUserRateEntity
import com.dezdeqness.contract.pinned.repository.PinnedUserRateRepository
import com.dezdeqness.data.datasource.db.PinnedUserRateDataSource

class PinnedUserRateRepositoryImpl(
    private val pinnedUserRateDataSource: PinnedUserRateDataSource,
) : PinnedUserRateRepository {
    override suspend fun getAllByGroupId(groupId: Long) =
        pinnedUserRateDataSource.getAllByGroupId(groupId = groupId)

    override suspend fun insert(pinnedUserRateLocal: PinnedUserRateEntity) {
        pinnedUserRateDataSource.insert(pinnedUserRateLocal = pinnedUserRateLocal)
    }

    override suspend fun deleteByUserRateId(userRateId: Long) {
        pinnedUserRateDataSource.deleteByUserRateId(userRateId = userRateId)
    }

    override suspend fun isPinned(userRateId: Long) =
        pinnedUserRateDataSource.isPinned(userRateId = userRateId)
}
