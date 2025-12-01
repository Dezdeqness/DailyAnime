package com.dezdeqness.data.mapper

import com.dezdeqness.contract.anime.model.ScreenshotEntity
import com.dezdeqness.data.DetailsQuery
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ScreenshotMapper @Inject constructor() {

    fun fromResponse(item: DetailsQuery.Screenshot) =
        ScreenshotEntity(
            original = item.originalUrl,
            preview = item.x332Url
        )

}
