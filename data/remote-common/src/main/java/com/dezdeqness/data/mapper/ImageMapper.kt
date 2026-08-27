package com.dezdeqness.data.mapper

import com.dezdeqness.contract.anime.model.ImageEntity
import com.dezdeqness.data.model.ImageRemote
import javax.inject.Inject

class ImageMapper @Inject constructor() {

    fun fromResponse(item: ImageRemote?) = if (item != null) {
        ImageEntity(
            original = item.original,
            preview = item.preview,
            x96 = item.x96,
            x48 = item.x48,
        )
    } else {
        ImageEntity()
    }
}
