package com.dezdeqness.feature.stats.di

import androidx.lifecycle.ViewModel
import com.dezdeqness.feature.stats.presentation.profile.ProfileStatsComposer
import com.dezdeqness.feature.stats.presentation.profile.ProfileStatsViewModel
import com.dezdeqness.foundation.di.ViewModelKey
import com.dezdeqness.foundation.provider.ResourceProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
abstract class StatsModule {

    companion object {
        @Provides
        internal fun provideProfileStatsComposer(resourceProvider: ResourceProvider) = ProfileStatsComposer(
            resourceProvider = resourceProvider,
        )
    }

    @Binds
    @IntoMap
    @ViewModelKey(ProfileStatsViewModel::class)
    internal abstract fun bindProfileStatsViewModel(viewModel: ProfileStatsViewModel): ViewModel
}
