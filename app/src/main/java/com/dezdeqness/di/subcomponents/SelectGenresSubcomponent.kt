package com.dezdeqness.di.subcomponents

import com.dezdeqness.di.core.BaseComponent
import com.dezdeqness.di.modules.SelectGenresModule
import dagger.Subcomponent

@Subcomponent(modules = [SelectGenresModule::class])
interface SelectGenresSubcomponent : BaseComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): SelectGenresSubcomponent
    }
}
