package com.dezdeqness.di.subcomponents

import com.dezdeqness.di.modules.DebugModule
import com.dezdeqness.presentation.features.debugscreen.DebugScreenActivity
import dagger.Subcomponent


@Subcomponent(modules = [DebugModule::class])
interface DebugComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): DebugComponent
    }

    fun inject(activity: DebugScreenActivity)

}
