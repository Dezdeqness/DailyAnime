package com.dezdeqness.di.subcomponents

import com.dezdeqness.di.core.BaseComponent
import com.dezdeqness.di.modules.AnimeChronologyModule
import com.dezdeqness.di.modules.GenericListableViewModelModule
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Named

@Subcomponent(
    modules = [
        AnimeChronologyModule::class,
        ChronologyArgsModule::class,
        GenericListableViewModelModule::class
    ]
)
interface AnimeChronologyComponent : BaseComponent {
    @Subcomponent.Builder
    interface Builder {

        fun argsModule(module: ChronologyArgsModule): Builder

        fun build(): AnimeChronologyComponent
    }
}

@Module
class ChronologyArgsModule(private val id: Long) {

    @Named("animeId")
    @Provides
    fun provideAnimeId() = id

}
