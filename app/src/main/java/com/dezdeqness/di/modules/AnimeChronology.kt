package com.dezdeqness.di.modules

import androidx.lifecycle.ViewModel
import com.dezdeqness.di.ViewModelKey
import com.dezdeqness.presentation.features.animechronology.AnimeChronologyViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(includes = [AnimeModule::class])
abstract class AnimeChronology {

    @Binds
    @IntoMap
    @ViewModelKey(AnimeChronologyViewModel::class)
    abstract fun bindAnimeChronologyViewModel(viewModel: AnimeChronologyViewModel): ViewModel

}
