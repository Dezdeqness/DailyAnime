package com.dezdeqness.di.source

import com.dezdeqness.contract.source.SourceConfig
import com.dezdeqness.di.subcomponents.AnimeComponent

interface SearchSourceComponent {
    val sourceConfig: SourceConfig

    fun animeComponent(): AnimeComponent.Factory
}
