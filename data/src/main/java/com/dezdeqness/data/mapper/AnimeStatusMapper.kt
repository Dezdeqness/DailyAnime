package com.dezdeqness.data.mapper

import com.dezdeqness.domain.model.AnimeStatusEntity
import javax.inject.Inject

class AnimeStatusMapper @Inject constructor() {

    fun map(status: String) =
        when (status) {
            "anons" -> AnimeStatusEntity.ANONS
            "ongoing" -> AnimeStatusEntity.ONGOING
            "released" -> AnimeStatusEntity.RELEASED
            "latest" -> AnimeStatusEntity.LATEST
            else -> AnimeStatusEntity.UNKNOWN
        }
}
