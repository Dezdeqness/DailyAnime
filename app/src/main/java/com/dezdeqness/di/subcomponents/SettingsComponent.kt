package com.dezdeqness.di.subcomponents

import androidx.lifecycle.ViewModel
import com.dezdeqness.core.di.ViewModelKey
import com.dezdeqness.data.core.AppLogger
import com.dezdeqness.di.core.BaseComponent
import com.dezdeqness.feature.settings.SettingsViewModel
import com.dezdeqness.feature.settings.store.SettingsActor
import com.dezdeqness.feature.settings.store.actors.AboutActor
import com.dezdeqness.feature.settings.store.actors.SectionActor
import com.dezdeqness.feature.settings.store.core.SettingsNamespace
import com.dezdeqness.feature.settings.store.core.settingsReducer
import com.dezdeqness.shared.presentation.bridge.ApplicationBridge
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import dagger.multibindings.IntoMap
import money.vivid.elmslie.core.store.ElmStore
import javax.inject.Singleton

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
        fun providerSectionActors(applicationBridge: ApplicationBridge): List<SectionActor> =
            listOf<SectionActor>(
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
