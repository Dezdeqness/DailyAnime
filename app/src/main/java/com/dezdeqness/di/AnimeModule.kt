package com.dezdeqness.di

import androidx.lifecycle.ViewModel
import com.dezdeqness.presentation.features.animelist.AnimeViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AnimeModule {
    @Binds
    @IntoMap
    @ViewModelKey(AnimeViewModel::class)
    abstract fun bindViewModel(viewModel: AnimeViewModel): ViewModel
}
