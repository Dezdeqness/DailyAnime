package com.dezdeqness.di

import android.content.Context
import com.dezdeqness.core.AppLogger
import com.dezdeqness.di.modules.AccountModule
import com.dezdeqness.di.modules.DataModule
import com.dezdeqness.di.modules.DatabaseModule
import com.dezdeqness.di.modules.RemoteModule
import com.dezdeqness.di.subcomponents.AnimeChronologyComponent
import com.dezdeqness.di.subcomponents.AnimeComponent
import com.dezdeqness.di.subcomponents.AnimeDetailsComponent
import com.dezdeqness.di.subcomponents.AnimeSearchFilterComponent
import com.dezdeqness.di.subcomponents.AnimeSimilarComponent
import com.dezdeqness.di.subcomponents.AnimeStatsComponent
import com.dezdeqness.di.subcomponents.AuthorizationComponent
import com.dezdeqness.di.subcomponents.CalendarComponent
import com.dezdeqness.di.subcomponents.HistoryComponent
import com.dezdeqness.di.subcomponents.MainComponent
import com.dezdeqness.di.subcomponents.PersonalListComponent
import com.dezdeqness.di.subcomponents.ProfileComponent
import com.dezdeqness.di.subcomponents.RoutingComponent
import com.dezdeqness.di.subcomponents.ScreenshotsViewerComponent
import com.dezdeqness.di.subcomponents.SettingsComponent
import com.dezdeqness.di.subcomponents.StatsComponent
import com.dezdeqness.di.subcomponents.PersonalListHostComponent
import com.dezdeqness.di.subcomponents.UserRateComponent
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

    fun authorizationComponent(): AuthorizationComponent.Builder

    fun animeDetailsComponent(): AnimeDetailsComponent.Builder

    fun animeSearchFilterComponent(): AnimeSearchFilterComponent.Builder

    fun personalListComponent(): PersonalListComponent.Factory

    fun editRateComponent(): UserRateComponent.Builder

    fun calendarComponent(): CalendarComponent.Factory

    fun unauthorizedHostComponent(): PersonalListHostComponent.Factory

    fun historyComponent(): HistoryComponent.Factory

    fun settingsComponent(): SettingsComponent.Factory

    fun statsComponent(): StatsComponent.Factory

    fun animeStatsComponent(): AnimeStatsComponent.Builder

    fun animeSimilarComponent(): AnimeSimilarComponent.Builder

    fun animeChronologyComponent(): AnimeChronologyComponent.Builder

    fun mainComponent(): MainComponent.Factory

    fun routingComponent(): RoutingComponent.Factory

    fun settingsRepository(): SettingsRepository

    fun screenshotsViewerComponent(): ScreenshotsViewerComponent.Builder

    val appLogger: AppLogger

}
