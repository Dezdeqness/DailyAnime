package com.dezdeqness.contract.anime

import com.dezdeqness.contract.anime.model.RelatedItemEntity
import com.dezdeqness.contract.anime.model.RoleEntity
import com.dezdeqness.contract.anime.model.ScreenshotEntity

typealias DetailsAdditionalInfo = Triple<
        List<ScreenshotEntity>,
        List<RelatedItemEntity>,
        List<RoleEntity>>
