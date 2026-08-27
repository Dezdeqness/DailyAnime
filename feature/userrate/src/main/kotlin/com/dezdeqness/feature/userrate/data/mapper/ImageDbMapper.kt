package com.dezdeqness.feature.userrate.data.mapper

import com.dezdeqness.contract.anime.model.ImageEntity
import com.dezdeqness.feature.userrate.data.database.ImageLocal
import javax.inject.Inject

internal class ImageDbMapper @Inject constructor() {

    fun fromDatabase(item: ImageLocal?) = if (item != null) {
        ImageEntity(
            original = item.original,
            preview = item.preview,
            x96 = item.x96,
            x48 = item.x48,
        )
    } else {
        ImageEntity()
    }

    fun toDatabase(item: ImageEntity) = ImageLocal(
        original = item.original,
        preview = item.preview,
        x96 = item.x96,
        x48 = item.x48,
    )
}
