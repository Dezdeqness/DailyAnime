package com.dezdeqness.di.subcomponents

import androidx.lifecycle.ViewModel
import com.dezdeqness.core.di.ViewModelKey
import com.dezdeqness.di.core.BaseComponent
import com.dezdeqness.feature.settings.SettingsViewModel
import dagger.Binds
import dagger.Module
import dagger.Subcomponent
import dagger.multibindings.IntoMap

@Subcomponent(modules = [SettingsModule::class])
interface SettingsComponent : BaseComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): SettingsComponent
    }
}

@Module
abstract class SettingsModule {

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    abstract fun bindSettingsViewModel(viewModel: SettingsViewModel): ViewModel

}
