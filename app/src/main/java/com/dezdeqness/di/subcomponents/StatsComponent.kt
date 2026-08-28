package com.dezdeqness.di.subcomponents

import com.dezdeqness.di.core.BaseComponent
import com.dezdeqness.feature.stats.di.StatsModule
import dagger.Subcomponent

@Subcomponent(modules = [StatsModule::class])
interface StatsComponent : BaseComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): StatsComponent
    }
}
