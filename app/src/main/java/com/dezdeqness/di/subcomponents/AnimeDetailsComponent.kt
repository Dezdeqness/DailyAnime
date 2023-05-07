package com.dezdeqness.di.subcomponents

import com.dezdeqness.di.modules.AnimeDetailsModule
import com.dezdeqness.presentation.animestats.AnimeStatsFragment
import com.dezdeqness.presentation.features.animedetails.AnimeDetailsFragment
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Named

@Subcomponent(modules = [
    AnimeDetailsModule::class, ArgsModule::class])
interface AnimeDetailsComponent {
    @Subcomponent.Builder
    interface Builder {

        fun argsModule(module: ArgsModule): Builder

        fun build(): AnimeDetailsComponent
    }

    fun inject(fragment: AnimeDetailsFragment)
    fun inject(fragment: AnimeStatsFragment)
}

@Module
class ArgsModule( @Named("animeId") private val id: Long) {

    @Named("animeId")
    @Provides
    fun provideAnimeId() = id

}

