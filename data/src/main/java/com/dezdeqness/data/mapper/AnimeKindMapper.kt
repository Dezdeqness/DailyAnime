package com.dezdeqness.data.mapper

import com.dezdeqness.domain.model.AnimeKindEntity
import javax.inject.Inject

class AnimeKindMapper @Inject constructor() {

    fun map(kind: String) =
        when (kind) {
            "tv" -> AnimeKindEntity.TV
            "movie" -> AnimeKindEntity.MOVIE
            "ova" -> AnimeKindEntity.OVA
            "ona" -> AnimeKindEntity.ONA
            "special" -> AnimeKindEntity.SPECIAL
            "music" -> AnimeKindEntity.MUSIC
            else -> AnimeKindEntity.UNKNOWN
        }

}
