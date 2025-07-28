package com.dezdeqness.di.subcomponents

import com.dezdeqness.di.core.BaseComponent
import com.dezdeqness.di.modules.AnimeSimilarModule
import com.dezdeqness.di.modules.GenericListableViewModelModule
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Named

@Subcomponent(modules = [AnimeSimilarModule::class, SimilarArgsModule::class, GenericListableViewModelModule::class])
interface AnimeSimilarComponent : BaseComponent {
    @Subcomponent.Builder
    interface Builder {

        fun argsModule(module: SimilarArgsModule): Builder

        fun build(): AnimeSimilarComponent
    }
}

@Module
class SimilarArgsModule(private val id: Long) {

    @Named("animeId")
    @Provides
    fun provideAnimeId() = id

}
