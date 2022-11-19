package com.dezdeqness.data.model.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dezdeqness.data.core.DataModel

@Entity(tableName = "account")
data class AccountLocal(
    @PrimaryKey val id: Long,

    @ColumnInfo(name = "nickname")
    val nickname: String,

    @ColumnInfo(name = "avatar")
    val avatar: String,

    @ColumnInfo(name = "last_online")
    val lastOnline: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "sex")
    val sex: String,
) : DataModel.Db
