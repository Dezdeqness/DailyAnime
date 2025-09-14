package com.dezdeqness.data.mapper

import com.dezdeqness.data.core.TimestampConverter
import com.dezdeqness.data.model.UserRateRemote
import com.dezdeqness.data.model.db.UserRateLocal
import com.dezdeqness.contract.anime.model.UserRateEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRatesMapper @Inject constructor(
    private val animeMapper: AnimeMapper,
    private val timestampConverter: TimestampConverter,
) {

    fun fromResponse(item: UserRateRemote) =
        UserRateEntity(
            id = item.id,
            score = item.score,
            status = item.status,
            text = item.text.orEmpty(),
            episodes = item.episodes ?: 0,
            chapters = item.chapters ?: 0,
            volumes = item.volumes ?: 0,
            textHTML = item.textHTML,
            rewatches = item.rewatches,
            createdAtTimestamp = timestampConverter.convertToTimeStampWithTime(item.createdAt),
            updatedAtTimestamp = timestampConverter.convertToTimeStampWithTime(item.updatedAt),
            anime = item.anime?.let
            { animeMapper.fromResponse(it) },
        )

    fun fromDatabase(item: UserRateLocal) =
        UserRateEntity(
            id = item.id,
            score = item.score,
            status = item.status,
            text = item.text,
            episodes = item.episodes,
            chapters = item.chapters,
            volumes = item.volumes,
            textHTML = item.textHTML,
            rewatches = item.rewatches,
            createdAtTimestamp = item.createdAtTimestamp,
            updatedAtTimestamp = item.updatedAtTimestamp,
            anime = item.anime?.let { animeMapper.fromDatabase(it) },
        )

    fun toDatabase(item: UserRateEntity) =
        UserRateLocal(
            id = item.id,
            score = item.score,
            status = item.status,
            text = item.text,
            episodes = item.episodes,
            chapters = item.chapters,
            volumes = item.volumes,
            textHTML = item.textHTML,
            rewatches = item.rewatches,
            createdAtTimestamp = item.createdAtTimestamp,
            updatedAtTimestamp = item.updatedAtTimestamp,
            anime = animeMapper.toDatabase(item.anime),
        )

}
