package com.dezdeqness.di

import com.dezdeqness.presentation.features.animelist.AnimeListFragment
import dagger.Subcomponent

@Subcomponent(modules = [DataModule::class])
interface AnimeComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): AnimeComponent
    }

    fun inject(fragment: AnimeListFragment)
}
