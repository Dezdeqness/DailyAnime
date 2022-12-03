package com.dezdeqness.data.mapper

import com.dezdeqness.data.model.ImageRemote
import com.dezdeqness.data.model.db.ImageLocal
import com.dezdeqness.domain.model.ImageEntity
import javax.inject.Inject

class ImageMapper @Inject constructor() {

    fun fromResponse(item: ImageRemote?) =
        if (item != null) {
            ImageEntity(
                original = item.original,
                preview = item.preview,
                x96 = item.x96,
                x48 = item.x48,
            )
        } else {
            ImageEntity()
        }

    fun fromDatabase(item: ImageLocal?) =
        if (item != null) {
            ImageEntity(
                original = item.original,
                preview = item.preview,
                x96 = item.x96,
                x48 = item.x48,
            )
        } else {
            ImageEntity()
        }

    fun toDatabase(item: ImageEntity) =
        ImageLocal(
            original = item.original,
            preview = item.preview,
            x96 = item.x96,
            x48 = item.x48,
        )

}
