package com.dezdeqness.di.subcomponents

import android.content.Context
import androidx.lifecycle.ViewModel
import com.dezdeqness.contract.auth.repository.AuthRepository
import com.dezdeqness.contract.settings.repository.SettingsRepository
import com.dezdeqness.core.di.ViewModelKey
import com.dezdeqness.data.core.AppLogger
import com.dezdeqness.data.core.config.ConfigManager
import com.dezdeqness.data.provider.AlarmManagerProvider
import com.dezdeqness.data.provider.HomeGenresProvider
import com.dezdeqness.data.provider.StatusesProvider
import com.dezdeqness.di.core.BaseComponent
import com.dezdeqness.feature.settings.SettingsViewModel
import com.dezdeqness.feature.settings.store.SettingsActor
import com.dezdeqness.feature.settings.store.actors.AboutActor
import com.dezdeqness.feature.settings.store.actors.ContentActor
import com.dezdeqness.feature.settings.store.actors.NavigationActor
import com.dezdeqness.feature.settings.store.actors.NotificationActor
import com.dezdeqness.feature.settings.store.actors.SectionActor
import com.dezdeqness.feature.settings.store.actors.StorageActor
import com.dezdeqness.feature.settings.store.actors.ThemeActor
import com.dezdeqness.feature.settings.store.core.SettingsNamespace
import com.dezdeqness.feature.settings.store.core.settingsReducer
import com.dezdeqness.shared.presentation.bridge.ApplicationBridge
import com.dezdeqness.shared.presentation.manager.WorkSchedulerManager
import com.dezdeqness.shared.presentation.mapper.PersonalRibbonMapper
import com.dezdeqness.shared.presentation.provider.PermissionCheckProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import dagger.multibindings.IntoMap
import money.vivid.elmslie.core.store.ElmStore

@Subcomponent(modules = [SettingsModule::class])
interface SettingsComponent : BaseComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): SettingsComponent
    }
}

@Module
abstract class SettingsModule {

    companion object {
        @Provides
        fun providerSectionActors(
            applicationBridge: ApplicationBridge,
            settingsRepository: SettingsRepository,
            authRepository: AuthRepository,
            homeGenresProvider: HomeGenresProvider,
            permissionCheckProvider: PermissionCheckProvider,
            alarmManagerProvider: AlarmManagerProvider,
            workSchedulerManager: WorkSchedulerManager,
            statusesProvider: StatusesProvider,
            ribbonMapper: PersonalRibbonMapper,
            configManager: ConfigManager,
            context: Context,
        ): List<SectionActor> =
            listOf(
                ThemeActor(settingsRepository = settingsRepository),
                NotificationActor(
                    settingsRepository = settingsRepository,
                    permissionCheckProvider = permissionCheckProvider,
                    alarmManagerProvider = alarmManagerProvider,
                    workSchedulerManager = workSchedulerManager,
                ),
                ContentActor(
                    settingsRepository = settingsRepository,
                    homeGenresProvider = homeGenresProvider,
                ),
                NavigationActor(
                    settingsRepository = settingsRepository,
                    authRepository = authRepository,
                    statusesProvider = statusesProvider,
                    ribbonMapper = ribbonMapper,
                    configManager = configManager,
                ),
                StorageActor(
                    settingsRepository = settingsRepository,
                    context = context,
                ),
                AboutActor(applicationBridge = applicationBridge),
            )

        @Provides
        fun provideSettingsActor(
            sectionActors: List<@JvmSuppressWildcards SectionActor>,
            appLogger: AppLogger,
        ) = SettingsActor(
            sectionActors = sectionActors,
            appLogger = appLogger,
        )

        @Provides
        fun provideHistoryStore(actor: SettingsActor): ElmStore<
                SettingsNamespace.Event,
                SettingsNamespace.State,
                SettingsNamespace.Effect,
                SettingsNamespace.Command> =
            ElmStore(
                initialState = SettingsNamespace.State(),
                reducer = settingsReducer,
                actor = actor,
            )
    }

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    abstract fun bindSettingsViewModel(viewModel: SettingsViewModel): ViewModel
}
