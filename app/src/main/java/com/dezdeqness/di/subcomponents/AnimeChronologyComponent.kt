package com.dezdeqness.di.subcomponents

import com.dezdeqness.di.modules.AnimeChronologyModule
import com.dezdeqness.di.modules.GenericListableViewModelModule
import com.dezdeqness.presentation.features.animechronology.AnimeChronologyFragment
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Named

@Subcomponent(modules = [AnimeChronologyModule::class, ChronologyArgsModule::class, GenericListableViewModelModule::class])
interface AnimeChronologyComponent {
    @Subcomponent.Builder
    interface Builder {

        fun argsModule(module: ChronologyArgsModule): Builder

        fun build(): AnimeChronologyComponent
    }

    fun inject(fragment: AnimeChronologyFragment)
}

@Module
class ChronologyArgsModule(private val id: Long) {

    @Named("animeId")
    @Provides
    fun provideAnimeId() = id

}
