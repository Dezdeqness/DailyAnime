package com.dezdeqness.data.model.db

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dezdeqness.data.core.DataModel

@Entity(tableName = "user_rate")
data class UserRateLocal(
    @PrimaryKey val id: Long,

    @ColumnInfo(name = "score")
    val score: Long,

    @ColumnInfo(name = "status")
    val status: String,

    @ColumnInfo(name = "text")
    val text: String,

    @ColumnInfo(name = "episodes")
    val episodes: Long,

    @ColumnInfo(name = "chapters")
    val chapters: Long,

    @ColumnInfo(name = "volumes")
    val volumes: Long,

    @ColumnInfo(name = "text_html")
    val textHTML: String,

    @ColumnInfo(name = "rewatches")
    val rewatches: Long,

    @ColumnInfo(name = "created_at")
    val createdAt: String,

    @ColumnInfo(name = "updated_at")
    val updatedAt: String,

    @Embedded
    val anime: AnimeLocal?,
) : DataModel.Db

data class AnimeLocal(
    @ColumnInfo(name = "anime_id")
    val id: Long,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "russian")
    val russian: String,

    @Embedded
    val image: ImageLocal?,

    @ColumnInfo(name = "url")
    val url: String,

    @ColumnInfo(name = "kind")
    val kind: String,

    @ColumnInfo(name = "anime_score")
    val score: Float,

    @ColumnInfo(name = "anime_status")
    val status: String,

    @ColumnInfo(name = "anime_episodes")
    val episodes: Int,

    @ColumnInfo(name = "anime_episodes_aired")
    val episodesAired: Int,

    @ColumnInfo(name = "aired_on")
    val airedOn: String?,

    @ColumnInfo(name = "released_on")
    val releasedOn: String,
)

data class ImageLocal(

    @ColumnInfo(name = "original")
    val original: String,

    @ColumnInfo(name = "preview")
    val preview: String,

    @ColumnInfo(name = "x96")
    val x96: String,

    @ColumnInfo(name = "x48")
    val x48: String,
)
