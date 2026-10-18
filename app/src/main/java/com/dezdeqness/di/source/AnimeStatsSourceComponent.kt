package com.dezdeqness.di.source

import com.dezdeqness.di.subcomponents.AnimeStatsComponent

interface AnimeStatsSourceComponent {
    fun animeStatsComponent(): AnimeStatsComponent.Builder
}
