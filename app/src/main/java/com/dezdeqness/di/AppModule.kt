package com.dezdeqness.di

import android.content.Context
import android.content.res.AssetManager
import com.dezdeqness.core.AppLogger
import com.dezdeqness.core.CoroutineDispatcherProvider
import com.dezdeqness.core.CoroutineDispatcherProviderImpl
import com.dezdeqness.core.MessageProvider
import com.dezdeqness.data.manager.PersonalListFilterManager
import com.dezdeqness.data.manager.TokenManager
import com.dezdeqness.data.mapper.FilterMapper
import com.dezdeqness.data.mapper.GenreMapper
import com.dezdeqness.data.model.FilterTypeAdapter
import com.dezdeqness.data.provider.ConfigurationProvider
import com.dezdeqness.data.provider.ResourceProvider
import com.dezdeqness.data.provider.SettingsProvider
import com.dezdeqness.data.repository.SettingsRepositoryImpl
import com.dezdeqness.domain.repository.SettingsRepository
import com.dezdeqness.presentation.action.ActionConsumer
import com.dezdeqness.presentation.message.MessageConsumer
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun provideAssetManager(context: Context): AssetManager = context.assets

    @Singleton
    @Provides
    fun provideConfigurationProvider(
        assetManager: AssetManager,
        genreMapper: GenreMapper,
        filterMapper: FilterMapper,
        moshi: Moshi,
    ) =
        ConfigurationProvider(
            assetManager = assetManager,
            genreMapper = genreMapper,
            filterMapper = filterMapper,
            moshi = moshi,
        )

    @Singleton
    @Provides
    fun provideTokenManager(context: Context) =
        TokenManager(context = context)

    @Singleton
    @Provides
    fun provideResourceProvider(context: Context) =
        ResourceProvider(context = context)

    @Singleton
    @Provides
    fun provideSettingsProvider(context: Context) =
        SettingsProvider(context = context)

    @Singleton
    @Provides
    fun provideMoshi(): Moshi =
        Moshi.Builder()
            .add(FilterTypeAdapter())
            .build()

    @Singleton
    @Provides
    fun provideCoroutineDispatcherProvider(): CoroutineDispatcherProvider =
        CoroutineDispatcherProviderImpl()

    @Singleton
    @Provides
    fun provideAppLogger() = AppLogger()

    @Provides
    fun provideActionConsumer() = ActionConsumer()

    @Singleton
    @Provides
    fun provideMessageConsumer() = MessageConsumer()

    @Singleton
    @Provides
    fun provideMessageProvider(resourceProvider: ResourceProvider) =
        MessageProvider(resourceProvider = resourceProvider)

    @Singleton
    @Provides
    fun providePersonalListFilter(context: Context) =
        PersonalListFilterManager(
            context = context,
        )

    @Singleton
    @Provides
    fun provideSettingsRepository(settingsProvider: SettingsProvider): SettingsRepository =
        SettingsRepositoryImpl(settingsProvider = settingsProvider)

}
