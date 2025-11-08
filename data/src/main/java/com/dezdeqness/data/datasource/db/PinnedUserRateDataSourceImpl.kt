package com.dezdeqness.data.datasource.db

import com.dezdeqness.contract.pinned.models.PinnedUserRateEntity
import com.dezdeqness.data.datasource.db.dao.PinnedUserRateDao
import com.dezdeqness.data.mapper.PinnedUserRateMapper
import javax.inject.Inject

class PinnedUserRateDataSourceImpl @Inject constructor(
    private val dao: PinnedUserRateDao,
    private val mapper: PinnedUserRateMapper,
) : PinnedUserRateDataSource {
    override suspend fun getAllByGroupId(groupId: Long) =
        dao.getAllByGroupId(groupId).map(mapper::fromDatabase)

    override suspend fun insert(pinnedUserRateLocal: PinnedUserRateEntity) {
        val local = mapper.toDatabase(pinnedUserRateLocal)
        dao.insert(local)
    }

    override suspend fun deleteByUserRateId(userRateId: Long) {
        dao.deleteByUserRateId(userRateId = userRateId)
    }

    override suspend fun isPinned(userRateId: Long) =
        dao.isPinned(userRateId = userRateId) > 0
}
