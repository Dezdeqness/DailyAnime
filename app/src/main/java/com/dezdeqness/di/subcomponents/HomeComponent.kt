package com.dezdeqness.di.subcomponents

import com.dezdeqness.di.core.BaseComponent
import com.dezdeqness.di.modules.HomeModule
import dagger.Subcomponent

@Subcomponent(modules = [HomeModule::class])
interface HomeComponent : BaseComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): HomeComponent
    }
}
