package com.dezdeqness.di.subcomponents

import com.dezdeqness.di.core.BaseComponent
import com.dezdeqness.di.modules.FavouriteModule
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Named

@Subcomponent(modules = [FavouriteModule::class, FavouriteArgsModule::class])
interface FavouriteSubcomponent : BaseComponent {
    @Subcomponent.Builder
    interface Builder {
        fun argsModule(module: FavouriteArgsModule): Builder
        fun build(): FavouriteSubcomponent
    }
}

@Module
class FavouriteArgsModule(private val userId: Long) {
    @Named("userId")
    @Provides
    fun provideUserId() = userId
}
