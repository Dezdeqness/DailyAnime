package com.dezdeqness.di

import android.content.Context
import com.dezdeqness.di.modules.AccountModule
import com.dezdeqness.di.modules.DataModule
import com.dezdeqness.di.modules.DatabaseModule
import com.dezdeqness.di.modules.RemoteModule
import com.dezdeqness.di.subcomponents.*
import com.dezdeqness.domain.repository.SettingsRepository
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

    fun calendarComponent(): CalendarComponent.Factory

    fun unauthorizedHostComponent(): UnauthorizedHostComponent.Factory

    fun unauthorizedComponent(): UnauthorizedComponent.Factory

    fun historyComponent(): HistoryComponent.Factory

    fun settingsComponent(): SettingsComponent.Factory

    fun statsComponent(): StatsComponent.Factory

    fun animeStatsComponent(): AnimeStatsComponent.Builder

    fun animeSimilarComponent(): AnimeSimilarComponent.Builder

    fun animeChronologyComponent(): AnimeChronologyComponent.Builder

    fun mainComponent(): MainComponent.Factory

    fun settingsRepository(): SettingsRepository

}
