package com.dezdeqness.data.mapper

import com.dezdeqness.data.DetailsQuery
import com.dezdeqness.data.model.ScreenshotRemote
import com.dezdeqness.contract.anime.model.ScreenshotEntity
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ScreenshotMapper @Inject constructor() {

    fun fromResponse(item: ScreenshotRemote) =
        ScreenshotEntity(
            original = item.original,
            preview = item.preview,
        )

    fun fromResponse(item: DetailsQuery.Screenshot) =
        ScreenshotEntity(
            original = item.originalUrl,
            preview = item.x332Url
        )

}
