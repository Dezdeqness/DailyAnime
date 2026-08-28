package com.dezdeqness.di.subcomponents

import com.dezdeqness.di.core.BaseComponent
import com.dezdeqness.feature.profile.di.ProfileModule
import dagger.Subcomponent

@Subcomponent(modules = [ProfileModule::class])
interface ProfileComponent : BaseComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): ProfileComponent
    }
}
