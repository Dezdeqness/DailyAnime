package com.dezdeqness.di.modules

import androidx.lifecycle.ViewModel
import com.dezdeqness.contract.settings.repository.UserInterestsProvider
import com.dezdeqness.feature.home.di.HomeModule as FeatureHomeModule
import com.dezdeqness.feature.home.presentation.HomeComposer
import com.dezdeqness.feature.home.presentation.HomeViewModel
import com.dezdeqness.foundation.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module(includes = [HistoryModule::class, FeatureHomeModule::class])
abstract class HomeModule {

    companion object {

        @Provides
        fun provideHomeComposer(userInterestsProvider: UserInterestsProvider) =
            HomeComposer(userInterestsProvider = userInterestsProvider)
    }

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindHomeViewModel(viewModel: HomeViewModel): ViewModel
}
