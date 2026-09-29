package com.dezdeqness.di.source.shikimori

import com.dezdeqness.contract.source.SourceConfig
import dagger.Module
import dagger.Provides

@Module
object ShikimoriSourceModule {

    @Provides
    fun provideSourceConfig(): SourceConfig = ShikimoriSourceConfig
}
