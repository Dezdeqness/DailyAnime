package com.dezdeqness.di.subcomponents

import com.dezdeqness.di.modules.HomeModule
import com.dezdeqness.presentation.features.home.HomeFragment
import dagger.Subcomponent

@Subcomponent(modules = [HomeModule::class])
interface HomeComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): HomeComponent
    }

    fun inject(fragment: HomeFragment)
}
