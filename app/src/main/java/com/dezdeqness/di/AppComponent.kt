package com.dezdeqness.di

import android.content.Context
import com.dezdeqness.contract.settings.repository.SettingsRepository
import com.dezdeqness.data.core.AppLogger
import com.dezdeqness.data.core.config.ConfigManager
import com.dezdeqness.di.modules.AccountStorageModule
import com.dezdeqness.di.modules.DataModule
import com.dezdeqness.di.source.shikimori.ShikimoriComponent
import com.dezdeqness.di.subcomponents.DebugComponent
import com.dezdeqness.feature.auth.di.AuthStorageModule
import com.dezdeqness.feature.userrate.di.UserRateStorageModule
import com.dezdeqness.foundation.coroutines.CoroutineDispatcherProvider
import com.dezdeqness.foundation.di.ViewModelBuilderModule
import com.dezdeqness.presentation.routing.ApplicationRouter
import com.dezdeqness.shared.presentation.manager.WorkSchedulerManager
import com.dezdeqness.shared.presentation.provider.PermissionCheckProvider
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ViewModelBuilderModule::class,
        AppModule::class,
        DataModule::class,
        AccountStorageModule::class,
        AuthStorageModule::class,
        UserRateStorageModule::class,
    ],
)
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun settingsRepository(): SettingsRepository

    fun coroutineDispatcherProvider(): CoroutineDispatcherProvider

    fun debugComponent(): DebugComponent.Factory

    fun shikimoriComponent(): ShikimoriComponent.Factory

    val appLogger: AppLogger

    val configManager: ConfigManager

    val applicationRouter: ApplicationRouter

    val settingsRepository: SettingsRepository

    val permissionCheckProvider: PermissionCheckProvider

    val workSchedulerManager: WorkSchedulerManager
}
