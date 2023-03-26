package com.dezdeqness.di.subcomponents

import com.dezdeqness.di.modules.PersonalListModule
import com.dezdeqness.presentation.features.personallist.PersonalListFragment
import dagger.Subcomponent

@Subcomponent(modules = [PersonalListModule::class])
interface PersonalListComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): PersonalListComponent
    }

    fun inject(fragment: PersonalListFragment)

}
