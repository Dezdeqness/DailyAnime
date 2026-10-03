package com.dezdeqness.di.source.shikimori

import com.dezdeqness.contract.source.SourceConfig
import com.dezdeqness.contract.source.SourceFeature
import com.dezdeqness.contract.source.SourceType

object ShikimoriSourceConfig : SourceConfig {
    override val type = SourceType.SHIKIMORI

    override val features: Set<SourceFeature> = setOf(
        ShikimoriSearch,
        ShikimoriSearchFilter,
        ShikimoriAnimeDetails,
        ShikimoriChronology,
        ShikimoriSimilar,
        ShikimoriAnimeStats,
        ShikimoriCharacterDetails,
        ShikimoriPersonDetails,
        ShikimoriCalendar,
        ShikimoriPersonalList,
        ShikimoriHistory,
        ShikimoriFavourites,
        ShikimoriAchievements,
        ShikimoriNews,
    )
}
