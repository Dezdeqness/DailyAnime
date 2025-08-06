package com.dezdeqness.data.model

import com.dezdeqness.data.core.DataModel
import com.squareup.moshi.Json

data class AchievementRemote(
    @field:Json(name = "id")
    val id: Long,

    @field:Json(name = "neko_id")
    val nekoId: String,

    @field:Json(name = "level")
    val level: Int,

    @field:Json(name = "progress")
    val progress: Long,

    @field:Json(name = "user_id")
    val userId: Long,

    @field:Json(name = "created_at")
    val createdAt: String,

    @field:Json(name = "updated_at")
    val updatedAt: String,

) : DataModel.Api
