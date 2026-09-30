package com.dezdeqness.di.source.shikimori

import androidx.datastore.core.DataStore
import com.dezdeqness.contract.source.SourceConfig
import com.dezdeqness.data.TokenEntityProto
import com.dezdeqness.data.manager.TokenManager
import com.dezdeqness.di.modules.TokenStorageModule
import com.dezdeqness.foundation.di.SourceScope
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
object ShikimoriSourceModule {

    @Provides
    fun provideSourceConfig(): SourceConfig = ShikimoriSourceConfig

    @SourceScope
    @Provides
    fun provideTokenManager(
        @Named(TokenStorageModule.SHIKIMORI_TOKEN) tokenStore: DataStore<TokenEntityProto>,
    ) = TokenManager(tokenStore)
}
