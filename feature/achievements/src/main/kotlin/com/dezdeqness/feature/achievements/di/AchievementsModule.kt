package com.dezdeqness.feature.achievements.di

import androidx.lifecycle.ViewModel
import com.dezdeqness.contract.achievements.repository.AchievementConfigRepository
import com.dezdeqness.contract.achievements.repository.AchievementRepository
import com.dezdeqness.feature.achievements.data.AchievementApiService
import com.dezdeqness.feature.achievements.data.AchievementConfigRepositoryImpl
import com.dezdeqness.feature.achievements.data.AchievementRemoteDataSource
import com.dezdeqness.feature.achievements.data.AchievementRemoteDataSourceImpl
import com.dezdeqness.feature.achievements.data.AchievementRepositoryImpl
import com.dezdeqness.feature.achievements.presentation.AchievementsViewModel
import com.dezdeqness.foundation.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import retrofit2.Retrofit

@Module
abstract class AchievementsModule {

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

    @Binds
    @IntoMap
    @ViewModelKey(AchievementsViewModel::class)
    internal abstract fun bindAchievementsViewModel(viewModel: AchievementsViewModel): ViewModel
}
