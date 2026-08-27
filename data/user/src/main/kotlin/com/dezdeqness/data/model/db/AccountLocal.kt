package com.dezdeqness.data.model.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

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

    @ColumnInfo(name = "anime_stats")
    val anime: List<StatusLocal>? = null,

    @ColumnInfo(name = "anime_scores")
    val scores: List<StatsLocal>? = null,

    @ColumnInfo(name = "anime_types")
    val types: List<StatsLocal>? = null,
)

data class StatusLocal(
    @ColumnInfo(name = "status_id")
    val statusId: Long,

    @ColumnInfo(name = "grouped_id")
    val groupedId: String,

    @ColumnInfo(name = "status_name")
    val name: String,

    @ColumnInfo(name = "size")
    val size: Long,

    @ColumnInfo(name = "type")
    val type: String,
)

data class StatsLocal(
    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "value")
    val value: Int,
)
