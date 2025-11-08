package com.dezdeqness.data.mapper

import com.dezdeqness.contract.pinned.models.PinnedUserRateEntity
import com.dezdeqness.data.model.db.PinnedUserRateLocal
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PinnedUserRateMapper @Inject constructor() {

    fun fromDatabase(item: PinnedUserRateLocal) =
        PinnedUserRateEntity(
            userRateId = item.userRateId,
            groupId = item.groupId,
        )

    fun toDatabase(item: PinnedUserRateEntity) =
        PinnedUserRateLocal(
            userRateId = item.userRateId,
            groupId = item.groupId,
        )

}
