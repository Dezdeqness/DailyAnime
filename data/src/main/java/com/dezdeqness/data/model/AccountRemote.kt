package com.dezdeqness.data.model

import com.dezdeqness.data.core.DataModel
import com.squareup.moshi.Json

open class AccountRemote : DataModel.Api {
    @field:Json(name = "id")
    val id: Long = 0

    @field:Json(name = "nickname")
    val nickname: String = ""

    @field:Json(name = "avatar")
    val avatar: String = ""

    @field:Json(name = "image")
    val image: ImageRemote? = null

    @field:Json(name = "last_online_at")
    val lastOnline: String = ""

    @field:Json(name = "name")
    val name: String? = null

    @field:Json(name = "sex")
    val sex: String? = null

    @field:Json(name = "stats")
    var stats: Stats? = null
}

data class Stats(
    @field:Json(name = "full_statuses")
    val fullStatuses: FullStatuses,
    @field:Json(name = "scores")
    val scores: StatsItemHolder,
    @field:Json(name = "types")
    val types: StatsItemHolder,
)

data class FullStatuses (
    @field:Json(name = "anime")
    val anime: List<Status>
)

data class StatsItemHolder(
    @field:Json(name = "anime")
    val items: List<StatsItem>,
)

data class Status (
    @field:Json(name = "id")
    val id: Long,

    @field:Json(name = "grouped_id")
    val groupedId: String,

    @field:Json(name = "name")
    val name: String,

    @field:Json(name = "size")
    val size: Long,

    @field:Json(name = "type")
    val type: String
)

data class StatsItem(
    @field:Json(name = "name")
    val name: String,

    @field:Json(name = "value")
    val value: Int,
)