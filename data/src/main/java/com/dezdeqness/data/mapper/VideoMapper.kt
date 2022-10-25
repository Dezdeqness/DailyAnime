package com.dezdeqness.data.mapper

import com.dezdeqness.data.model.VideoRemote
import com.dezdeqness.domain.model.VideoEntity
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class VideoMapper @Inject constructor() {

    fun fromResponse(item: VideoRemote) =
        VideoEntity(
            id = item.id,
            name = item.name.orEmpty(),
            playerUrl = item.playerUrl,
            url = item.url,
            imageUrl = item.imageUrl,
            kind = item.kind,
            hosting = item.hosting,
        )

}
