package com.dezdeqness.di.subcomponents

import com.dezdeqness.di.modules.AnimeListModule
import com.dezdeqness.di.modules.AnimeSearchFilterModule
import com.dezdeqness.presentation.features.animelist.AnimeListFragment
import dagger.Subcomponent

@Subcomponent(modules = [AnimeListModule::class, AnimeSearchFilterModule::class])
interface AnimeComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): AnimeComponent
    }

    fun inject(fragment: AnimeListFragment)
}
