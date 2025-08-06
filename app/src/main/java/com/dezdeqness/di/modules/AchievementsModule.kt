package com.dezdeqness.di.modules

import androidx.lifecycle.ViewModel
import com.dezdeqness.data.AchievementService
import com.dezdeqness.data.datasource.AchievementRemoteDataSource
import com.dezdeqness.data.datasource.AchievementRemoteDataSourceImpl
import com.dezdeqness.data.repository.AchievementRepositoryImpl
import com.dezdeqness.di.ViewModelKey
import com.dezdeqness.domain.repository.AchievementRepository
import com.dezdeqness.feature.achievements.presentation.AchievementsViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import retrofit2.Retrofit

@Module
abstract class AchievementsModule {
    companion object {
        @Provides
        fun provideAchievementService(retrofit: Retrofit): AchievementService =
            retrofit.create(AchievementService::class.java)
    }

    @Binds
    abstract fun bindAchievementRemoteDataSource(achievementRemoteDataSourceImpl: AchievementRemoteDataSourceImpl): AchievementRemoteDataSource

    @Binds
    abstract fun bindAchievementRepository(achievementRepositoryImpl: AchievementRepositoryImpl): AchievementRepository

    @Binds
    @IntoMap
    @ViewModelKey(AchievementsViewModel::class)
    abstract fun bindAchievementsViewModel(viewModel: AchievementsViewModel): ViewModel

}
