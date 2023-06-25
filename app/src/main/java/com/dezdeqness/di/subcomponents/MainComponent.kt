package com.dezdeqness.di.subcomponents

import com.dezdeqness.di.modules.MainModule
import com.dezdeqness.presentation.MainActivity
import dagger.Subcomponent

@Subcomponent(modules = [MainModule::class])
interface MainComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): MainComponent
    }

    fun inject(activity: MainActivity)

}
