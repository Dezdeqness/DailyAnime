package com.dezdeqness.di

import android.content.Context
import com.dezdeqness.di.modules.AccountModule
import com.dezdeqness.di.modules.DataModule
import com.dezdeqness.di.modules.DatabaseModule
import com.dezdeqness.di.subcomponents.EditRateComponent
import com.dezdeqness.di.modules.RemoteModule
import com.dezdeqness.di.subcomponents.AnimeComponent
import com.dezdeqness.di.subcomponents.AnimeDetailsComponent
import com.dezdeqness.di.subcomponents.AnimeSearchFilterComponent
import com.dezdeqness.di.subcomponents.AuthorizationComponent
import com.dezdeqness.di.subcomponents.PersonalListComponent
import com.dezdeqness.di.subcomponents.ProfileComponent
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
        AccountModule::class,
        DataModule::class,
        DatabaseModule::class,
    ]
)
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun animeComponent(): AnimeComponent.Factory

    fun profileComponent(): ProfileComponent.Factory

    fun authorizationComponent(): AuthorizationComponent.Factory
    fun animeDetailsComponent(): AnimeDetailsComponent.Builder

    fun animeSearchFilterComponent(): AnimeSearchFilterComponent.Builder

    fun personalListComponent(): PersonalListComponent.Factory

    fun editRateComponent(): EditRateComponent.Builder

}
