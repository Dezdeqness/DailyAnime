package com.dezdeqness.di.subcomponents

import androidx.lifecycle.ViewModel
import com.dezdeqness.di.ViewModelKey
import com.dezdeqness.presentation.features.settings.SettingsFragment
import com.dezdeqness.presentation.features.settings.SettingsViewModel
import dagger.Binds
import dagger.Module
import dagger.Subcomponent
import dagger.multibindings.IntoMap

@Subcomponent(modules = [SettingsModule::class])
interface SettingsComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): SettingsComponent
    }

    fun inject(fragment: SettingsFragment)

}

@Module
abstract class SettingsModule {

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    abstract fun bindSettingsViewModel(viewModel: SettingsViewModel): ViewModel

}
