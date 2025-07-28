package com.dezdeqness.di.subcomponents

import com.dezdeqness.di.core.BaseComponent
import com.dezdeqness.di.modules.AnimeListModule
import com.dezdeqness.di.modules.AnimeSearchFilterModule
import dagger.Subcomponent

@Subcomponent(modules = [AnimeListModule::class, AnimeSearchFilterModule::class])
interface AnimeComponent : BaseComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): AnimeComponent
    }
}
