package com.dezdeqness.data.mapper

import com.dezdeqness.contract.anime.model.VideoEntity
import com.dezdeqness.data.AnimeDetailsQuery
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VideoMapper @Inject constructor() {

    fun fromResponseGraphql(item: AnimeDetailsQuery.Video) =
        VideoEntity(
            id = item.id.toInt(),
            name = item.name.orEmpty(),
            playerUrl = item.playerUrl,
            url = item.url,
            imageUrl = if (item.imageUrl.contains(HTTPS)) item.imageUrl else HTTPS + item.imageUrl,
            kind = item.kind.rawValue,
            hosting = "",
        )

    companion object {
        private const val HTTPS = "http:"
    }

}
