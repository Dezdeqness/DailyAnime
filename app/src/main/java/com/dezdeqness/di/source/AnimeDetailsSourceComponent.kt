package com.dezdeqness.di.source

import com.dezdeqness.di.subcomponents.AnimeDetailsFeatureComponent

interface AnimeDetailsSourceComponent {
    fun animeDetailsFeatureComponent(): AnimeDetailsFeatureComponent.Factory
}
