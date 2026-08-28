package com.dezdeqness.di.subcomponents

import com.dezdeqness.di.core.BaseComponent
import com.dezdeqness.feature.onboarding.di.OnboardingModule
import dagger.Subcomponent

@Subcomponent(modules = [OnboardingModule::class])
interface SelectGenresSubcomponent : BaseComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): SelectGenresSubcomponent
    }
}
