package com.dezdeqness.di.modules

import androidx.lifecycle.ViewModel
import com.dezdeqness.feature.favourite.presentation.FavouritesViewModel
import com.dezdeqness.foundation.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class FavouriteFeatureModule {

    @Binds
    @IntoMap
    @ViewModelKey(FavouritesViewModel::class)
    abstract fun bindFavouritesViewModel(viewModel: FavouritesViewModel): ViewModel
}
