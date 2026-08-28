package com.dezdeqness.feature.favourite.di

import androidx.lifecycle.ViewModel
import com.dezdeqness.feature.favourite.presentation.FavouritesViewModel
import com.dezdeqness.foundation.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class FavouritePresentationModule {

    @Binds
    @IntoMap
    @ViewModelKey(FavouritesViewModel::class)
    internal abstract fun bindFavouritesViewModel(viewModel: FavouritesViewModel): ViewModel
}
