package com.dezdeqness.data.mapper

import com.dezdeqness.data.model.ScreenshotRemote
import com.dezdeqness.domain.model.ScreenshotEntity
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ScreenshotMapper @Inject constructor() {

    fun fromResponse(item: ScreenshotRemote) =
        ScreenshotEntity(
            original = item.original,
            preview = item.preview,
        )

}
