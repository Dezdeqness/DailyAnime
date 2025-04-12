package com.dezdeqness.domain

import com.dezdeqness.domain.model.RelatedItemEntity
import com.dezdeqness.domain.model.RoleEntity
import com.dezdeqness.domain.model.ScreenshotEntity

typealias DetailsAdditionalInfo = Triple<
        List<ScreenshotEntity>,
        List<RelatedItemEntity>,
        List<RoleEntity>>
