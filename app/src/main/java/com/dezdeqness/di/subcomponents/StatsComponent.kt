package com.dezdeqness.di.subcomponents

import com.dezdeqness.di.modules.StatsModule
import com.dezdeqness.presentation.features.stats.StatsFragment
import dagger.Subcomponent

@Subcomponent(modules = [StatsModule::class])
interface StatsComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): StatsComponent
    }

    fun inject(fragment: StatsFragment)
}
