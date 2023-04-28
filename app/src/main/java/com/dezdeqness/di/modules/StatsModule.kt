package com.dezdeqness.di.modules

import androidx.lifecycle.ViewModel
import com.dezdeqness.data.provider.ResourceProvider
import com.dezdeqness.di.ViewModelKey
import com.dezdeqness.presentation.features.stats.StatsComposer
import com.dezdeqness.presentation.features.stats.StatsViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
abstract class StatsModule {

    companion object {
        @Provides
        fun provideStatsComposer(
            resourceProvider: ResourceProvider,
        ) = StatsComposer(
            resourceProvider = resourceProvider,
        )
    }

    @Binds
    @IntoMap
    @ViewModelKey(StatsViewModel::class)
    abstract fun bindStatsViewModel(viewModel: StatsViewModel): ViewModel

}
