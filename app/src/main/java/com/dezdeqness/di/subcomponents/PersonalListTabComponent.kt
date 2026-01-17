package com.dezdeqness.di.subcomponents

import com.dezdeqness.di.core.BaseComponent
import com.dezdeqness.di.modules.PersonalListTabModule
import dagger.Subcomponent

@Subcomponent(modules = [PersonalListTabModule::class])
interface PersonalListTabComponent : BaseComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): PersonalListTabComponent
    }
}
