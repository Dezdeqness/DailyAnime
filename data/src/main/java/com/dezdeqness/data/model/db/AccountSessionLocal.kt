package com.dezdeqness.data.model.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "accounts")
data class AccountSessionLocal(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "account_type") val accountType: String,
    @ColumnInfo(name = "is_active") val isActive: Boolean,
)
