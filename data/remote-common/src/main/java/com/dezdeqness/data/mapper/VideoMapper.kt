package com.dezdeqness.data.mapper

import com.dezdeqness.contract.anime.model.VideoEntity
import com.dezdeqness.contract.anime.model.VideoKind
import com.dezdeqness.data.AnimeDetailsQuery
import com.dezdeqness.data.type.VideoKindEnum
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VideoMapper @Inject constructor() {

    fun fromResponseGraphql(item: AnimeDetailsQuery.Video) = VideoEntity(
        id = item.id.toInt(),
        name = item.name?.takeIf { it.isNotBlank() },
        playerUrl = item.playerUrl,
        url = item.url,
        imageUrl = if (item.imageUrl.contains(HTTPS)) item.imageUrl else HTTPS + item.imageUrl,
        kind = videoKinds[item.kind] ?: VideoKind.OTHER,
        hosting = "",
    )

    companion object {
        private const val HTTPS = "http:"

        private val videoKinds = mapOf(
            VideoKindEnum.pv to VideoKind.TRAILER,
            VideoKindEnum.character_trailer to VideoKind.CHARACTER_TRAILER,
            VideoKindEnum.cm to VideoKind.COMMERCIAL,
            VideoKindEnum.op to VideoKind.OPENING,
            VideoKindEnum.ed to VideoKind.ENDING,
            VideoKindEnum.op_ed_clip to VideoKind.MUSIC,
            VideoKindEnum.clip to VideoKind.CLIP,
            VideoKindEnum.episode_preview to VideoKind.EPISODE_PREVIEW,
        )
    }
}
