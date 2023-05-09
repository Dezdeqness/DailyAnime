package com.dezdeqness.di.modules

import androidx.lifecycle.ViewModel
import com.dezdeqness.di.ViewModelKey

import com.dezdeqness.presentation.features.animesimilar.AnimeSimilarViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(includes = [AnimeModule::class])
abstract class AnimeSimilarModule {

    @Binds
    @IntoMap
    @ViewModelKey(AnimeSimilarViewModel::class)
    abstract fun bindAnimeSimilarViewModel(viewModel: AnimeSimilarViewModel): ViewModel

}
