package com.dezdeqness.di.subcomponents

import com.dezdeqness.di.core.BaseComponent
import com.dezdeqness.di.modules.PersonalListModule
import dagger.Subcomponent

@Subcomponent(modules = [PersonalListModule::class])
interface PersonalListComponent : BaseComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): PersonalListComponent
    }
}
