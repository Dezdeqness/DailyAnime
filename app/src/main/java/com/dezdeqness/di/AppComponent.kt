package com.dezdeqness.di

import android.content.Context
import com.dezdeqness.di.modules.RemoteModule
import com.dezdeqness.di.subcomponents.AnimeComponent
import com.dezdeqness.di.subcomponents.AnimeDetailsComponent
import com.dezdeqness.di.subcomponents.AnimeSearchFilterComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ViewModelBuilderModule::class,
        AppSubcomponents::class,
        RemoteModule::class,
        AppModule::class,
    ]
)
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun animeComponent(): AnimeComponent.Factory

    fun animeDetailsComponent(): AnimeDetailsComponent.Builder

    fun animeSearchFilterComponent(): AnimeSearchFilterComponent.Builder

}
