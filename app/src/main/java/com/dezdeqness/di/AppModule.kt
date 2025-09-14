package com.dezdeqness.di

import android.content.Context
import android.content.res.AssetManager
import com.dezdeqness.contract.settings.repository.SettingsRepository
import com.dezdeqness.data.core.AppLogger
import com.dezdeqness.core.MessageProvider
import com.dezdeqness.core.coroutines.CoroutineDispatcherProvider
import com.dezdeqness.core.coroutines.CoroutineDispatcherProviderImpl
import com.dezdeqness.data.analytics.AnalyticsManager
import com.dezdeqness.data.analytics.impl.AnalyticsManagerImpl
import com.dezdeqness.data.core.config.ConfigManager
import com.dezdeqness.data.core.config.ConfigSettingsProvider
import com.dezdeqness.data.core.config.local.DebugConfigProvider
import com.dezdeqness.data.core.config.remote.RemoteConfigProvider
import com.dezdeqness.data.manager.PersonalListFilterManager
import com.dezdeqness.data.manager.TokenManager
import com.dezdeqness.data.mapper.FilterMapper
import com.dezdeqness.data.mapper.GenreMapper
import com.dezdeqness.data.model.FilterTypeAdapter
import com.dezdeqness.data.provider.ConfigurationProvider
import com.dezdeqness.data.provider.ResourceProvider
import com.dezdeqness.data.repository.SettingsRepositoryImpl
import com.dezdeqness.contract.user.repository.UserRepository
import com.dezdeqness.data.mapper.AchievementMapper
import com.dezdeqness.data.provider.HistorySearchListProvider
import com.dezdeqness.data.repository.HistorySearchRepositoryImpl
import com.dezdeqness.domain.repository.HistorySearchRepository
import com.dezdeqness.presentation.action.ActionConsumer
import com.dezdeqness.presentation.message.MessageConsumer
import com.dezdeqness.presentation.routing.ApplicationRouter
import com.google.firebase.analytics.FirebaseAnalytics
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
        achievementMapper: AchievementMapper,
        moshi: Moshi,
    ) =
        ConfigurationProvider(
            assetManager = assetManager,
            genreMapper = genreMapper,
            filterMapper = filterMapper,
            achievementMapper = achievementMapper,
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
    fun provideMoshi(): Moshi =
        Moshi.Builder()
            .add(FilterTypeAdapter())
            .build()

    @Singleton
    @Provides
    fun provideHistorySearchRepository(historySearchListProvider: HistorySearchListProvider): HistorySearchRepository =
        HistorySearchRepositoryImpl(historySearchProvider = historySearchListProvider)

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
    fun provideRemoteConfigProvider(appLogger: AppLogger) = RemoteConfigProvider(
        appLogger = appLogger,
    )

    @Singleton
    @Provides
    fun provideDebugConfigProvider(context: Context) = DebugConfigProvider(
        context = context,
    )

    @Singleton
    @Provides
    fun provideConfigSettingsProvider(context: Context) = ConfigSettingsProvider(
        context = context,
    )

    @Singleton
    @Provides
    fun provideConfigManager(
        remoteConfigProvider: RemoteConfigProvider,
        debugConfigProvider: DebugConfigProvider,
        configSettingsProvider: ConfigSettingsProvider,
        appLogger: AppLogger,
    ) = ConfigManager(
        appLogger = appLogger,
        debugConfigProvider = debugConfigProvider,
        remoteConfigProvider = remoteConfigProvider,
        configSettingsProvider = configSettingsProvider,
    )

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
    fun provideSettingsRepository(
        context: Context,
    ): SettingsRepository =
        SettingsRepositoryImpl(
            context = context,
        )

    @Singleton
    @Provides
    fun provideApplicationRouter() = ApplicationRouter()

    @Singleton
    @Provides
    fun provideFirebaseAnalytics(context: Context) = FirebaseAnalytics.getInstance(context)

    @Singleton
    @Provides
    fun provideAnalyticsManager(
        userRepository: UserRepository,
        firebaseAnalytics: FirebaseAnalytics
    ): AnalyticsManager =
        AnalyticsManagerImpl(
            userRepository = userRepository,
            firebaseAnalytics = firebaseAnalytics
        )

}
