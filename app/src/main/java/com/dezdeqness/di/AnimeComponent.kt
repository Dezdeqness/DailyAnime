package com.dezdeqness.di

import com.dezdeqness.presentation.MainActivity
import dagger.Subcomponent

@Subcomponent(modules = [DataModule::class])
interface AnimeComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): AnimeComponent
    }

    fun inject(activity: MainActivity)
}
