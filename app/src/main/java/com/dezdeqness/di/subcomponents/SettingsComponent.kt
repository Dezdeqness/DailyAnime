package com.dezdeqness.di.subcomponents

import com.dezdeqness.di.core.BaseComponent
import com.dezdeqness.feature.settings.di.SettingsModule
import dagger.Subcomponent

@Subcomponent(modules = [SettingsModule::class])
interface SettingsComponent : BaseComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): SettingsComponent
    }
}
