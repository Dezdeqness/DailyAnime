package com.dezdeqness.di.subcomponents

import com.dezdeqness.di.modules.AnimeSimilarModule
import com.dezdeqness.presentation.features.animesimilar.AnimeSimilarFragment
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Named

@Subcomponent(modules = [AnimeSimilarModule::class, SimilarArgsModule::class])
interface AnimeSimilarComponent {
    @Subcomponent.Builder
    interface Builder {

        fun argsModule(module: SimilarArgsModule): Builder

        fun build(): AnimeSimilarComponent
    }

    fun inject(fragment: AnimeSimilarFragment)
}

@Module
class SimilarArgsModule(private val id: Long) {

    @Named("animeId")
    @Provides
    fun provideAnimeId() = id

}
