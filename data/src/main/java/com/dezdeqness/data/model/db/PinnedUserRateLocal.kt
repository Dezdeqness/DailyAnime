package com.dezdeqness.data.model.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pinned_user_rate")
data class PinnedUserRateLocal(
    @PrimaryKey
    val userRateId: Long,
    val groupId: String,
)
