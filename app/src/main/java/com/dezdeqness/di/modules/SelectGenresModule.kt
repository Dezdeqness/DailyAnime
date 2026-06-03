package com.dezdeqness.di.modules

import androidx.lifecycle.ViewModel
import com.dezdeqness.feature.onboarding.selectgenres.presentation.SelectGenresViewModel
import com.dezdeqness.foundation.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class SelectGenresModule {

    @Binds
    @IntoMap
    @ViewModelKey(SelectGenresViewModel::class)
    abstract fun bindSelectGenresViewModel(viewModel: SelectGenresViewModel): ViewModel
}
