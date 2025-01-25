package com.dezdeqness.di.subcomponents

import com.dezdeqness.di.modules.AnimeSearchFilterModule
import com.dezdeqness.presentation.features.animelist.AnimeListFragment
import dagger.Subcomponent

@Subcomponent(modules = [AnimeSearchFilterModule::class])
interface AnimeSearchFilterComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): AnimeSearchFilterComponent
    }

    fun inject(fragment: AnimeListFragment)
}
