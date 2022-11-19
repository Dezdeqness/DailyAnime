package com.dezdeqness.di.subcomponents

import com.dezdeqness.di.modules.ProfileModule
import com.dezdeqness.presentation.features.profile.ProfileFragment
import dagger.Subcomponent

@Subcomponent(modules = [ProfileModule::class])
interface ProfileComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): ProfileComponent
    }

    fun inject(fragment: ProfileFragment)

}
