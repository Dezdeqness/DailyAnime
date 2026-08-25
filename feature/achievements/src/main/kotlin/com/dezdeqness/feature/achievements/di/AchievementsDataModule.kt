package com.dezdeqness.feature.achievements.di

import com.dezdeqness.contract.achievements.repository.AchievementConfigRepository
import com.dezdeqness.contract.achievements.repository.AchievementRepository
import com.dezdeqness.feature.achievements.data.AchievementApiService
import com.dezdeqness.feature.achievements.data.AchievementConfigRepositoryImpl
import com.dezdeqness.feature.achievements.data.AchievementRemoteDataSource
import com.dezdeqness.feature.achievements.data.AchievementRemoteDataSourceImpl
import com.dezdeqness.feature.achievements.data.AchievementRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
abstract class AchievementsDataModule {

    companion object {
        @Provides
        internal fun provideAchievementApiService(retrofit: Retrofit): AchievementApiService =
            retrofit.create(AchievementApiService::class.java)
    }

    @Binds
    internal abstract fun bindAchievementRemoteDataSource(
        impl: AchievementRemoteDataSourceImpl,
    ): AchievementRemoteDataSource

    @Binds
    internal abstract fun bindAchievementRepository(
        impl: AchievementRepositoryImpl,
    ): AchievementRepository

    @Binds
    internal abstract fun bindAchievementConfigRepository(
        impl: AchievementConfigRepositoryImpl,
    ): AchievementConfigRepository
}
