package com.dezdeqness.di.source.anilist

import com.dezdeqness.contract.source.SourceConfig
import com.dezdeqness.data.analytics.AnalyticsManager
import com.dezdeqness.foundation.di.SourceScope
import dagger.Module
import dagger.Provides

@Module
object AnilistSourceModule {

    @Provides
    fun provideSourceConfig(): SourceConfig = AnilistSourceConfig

    @SourceScope
    @Provides
    fun provideAnalyticsManager(): AnalyticsManager = AnilistAnalyticsManager()
}
