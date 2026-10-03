package com.dezdeqness.di.source.anilist

import com.dezdeqness.contract.source.SourceConfig
import com.dezdeqness.contract.source.SourceFeature
import com.dezdeqness.contract.source.SourceType

object AnilistSourceConfig : SourceConfig {
    override val type = SourceType.ANILIST

    override val features: Set<SourceFeature> = setOf(
        AnilistSearch,
    )
}
