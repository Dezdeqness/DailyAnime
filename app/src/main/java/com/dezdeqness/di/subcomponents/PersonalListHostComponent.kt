package com.dezdeqness.di.subcomponents

import com.dezdeqness.di.modules.PersonalListHostModule
import com.dezdeqness.presentation.features.personallist.host.PersonalListHostFragment
import dagger.Subcomponent


@Subcomponent(modules = [PersonalListHostModule::class])
interface PersonalListHostComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): PersonalListHostComponent
    }

    fun inject(fragment: PersonalListHostFragment)

}
