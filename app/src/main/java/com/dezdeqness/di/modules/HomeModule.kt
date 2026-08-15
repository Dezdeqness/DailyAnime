package com.dezdeqness.di.modules

import androidx.lifecycle.ViewModel
import com.dezdeqness.contract.home.repository.HomeRepository
import com.dezdeqness.data.datasource.HomeRemoteDatasource
import com.dezdeqness.data.datasource.HomeRemoteDatasourceImpl
import com.dezdeqness.data.provider.HomeGenresProvider
import com.dezdeqness.data.repository.HomeRepositoryImpl
import com.dezdeqness.feature.home.presentation.HomeComposer
import com.dezdeqness.feature.home.presentation.HomeViewModel
import com.dezdeqness.foundation.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module(includes = [HistoryModule::class])
abstract class HomeModule {

    companion object {

        @Provides
        fun provideHomeComposer(homeGenresProvider: HomeGenresProvider) =
            HomeComposer(homeGenresProvider = homeGenresProvider)
    }

    @Binds
    abstract fun bindHomeRepository(repository: HomeRepositoryImpl): HomeRepository

    @Binds
    abstract fun bindHomeRemoteDataSource(dataSourceImpl: HomeRemoteDatasourceImpl): HomeRemoteDatasource

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindHomeViewModel(viewModel: HomeViewModel): ViewModel
}
