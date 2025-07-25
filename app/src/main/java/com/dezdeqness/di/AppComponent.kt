package com.dezdeqness.di

import android.content.Context
import com.dezdeqness.core.WorkSchedulerManager
import com.dezdeqness.core.coroutines.CoroutineDispatcherProvider
import com.dezdeqness.data.analytics.AnalyticsManager
import com.dezdeqness.data.core.AppLogger
import com.dezdeqness.data.provider.PermissionCheckProvider
import com.dezdeqness.di.modules.AccountModule
import com.dezdeqness.di.modules.DataModule
import com.dezdeqness.di.modules.DatabaseModule
import com.dezdeqness.di.modules.RemoteModule
import com.dezdeqness.di.subcomponents.AnimeChronologyComponent
import com.dezdeqness.di.subcomponents.AnimeComponent
import com.dezdeqness.di.subcomponents.AnimeDetailsComponent
import com.dezdeqness.di.subcomponents.AnimeSimilarComponent
import com.dezdeqness.di.subcomponents.AnimeStatsComponent
import com.dezdeqness.di.subcomponents.AuthorizationComponent
import com.dezdeqness.di.subcomponents.CalendarComponent
import com.dezdeqness.di.subcomponents.DebugComponent
import com.dezdeqness.di.subcomponents.HistoryComponent
import com.dezdeqness.di.subcomponents.HomeComponent
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
import com.dezdeqness.presentation.routing.ApplicationRouter
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

    fun coroutineDispatcherProvider(): CoroutineDispatcherProvider

    fun screenshotsViewerComponent(): ScreenshotsViewerComponent.Builder

    fun homeComponent(): HomeComponent.Factory

    fun debugComponent(): DebugComponent.Factory

    val appLogger: AppLogger

    val applicationRouter: ApplicationRouter

    val analyticsManager: AnalyticsManager

    val settingsRepository: SettingsRepository

    val permissionCheckProvider: PermissionCheckProvider

    val workSchedulerManager: WorkSchedulerManager
}
