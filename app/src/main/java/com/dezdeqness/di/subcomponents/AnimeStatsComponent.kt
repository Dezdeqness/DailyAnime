package com.dezdeqness.di.subcomponents

import com.dezdeqness.di.core.BaseComponent
import com.dezdeqness.di.modules.AnimeStatsModule
import com.dezdeqness.presentation.features.animestats.AnimeStatsArguments
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Named

@Subcomponent(modules = [AnimeStatsModule::class])
interface AnimeStatsComponent : BaseComponent {

    @Subcomponent.Builder
    interface Builder {

        fun argsModule(module: AnimeStatsArgsModule): Builder

        fun build(): AnimeStatsComponent
    }
}

@Module
class AnimeStatsArgsModule(private val arguments: AnimeStatsArguments) {

    @Named("animeStatsArguments")
    @Provides
    fun provideAnimeStatsArguments() = arguments

}
