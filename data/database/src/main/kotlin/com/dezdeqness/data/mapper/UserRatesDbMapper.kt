package com.dezdeqness.data.mapper

import com.dezdeqness.contract.anime.model.UserRateEntity
import com.dezdeqness.data.model.db.UserRateLocal
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRatesDbMapper @Inject constructor(
    private val animeDbMapper: AnimeDbMapper,
) {

    fun fromDatabase(item: UserRateLocal) = UserRateEntity(
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
        anime = item.anime?.let { animeDbMapper.fromDatabase(it) },
    )

    fun toDatabase(item: UserRateEntity) = UserRateLocal(
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
        anime = animeDbMapper.toDatabase(item.anime),
    )
}
