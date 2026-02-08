package com.dezdeqness.di.subcomponents

import com.dezdeqness.di.core.BaseComponent
import com.dezdeqness.di.modules.PersonalListSearchModule
import dagger.Subcomponent

@Subcomponent(modules = [PersonalListSearchModule::class])
interface PersonalListSearchComponent : BaseComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): PersonalListSearchComponent
    }
}
