package com.dezdeqness.di.subcomponents

import com.dezdeqness.di.core.BaseComponent
import com.dezdeqness.di.modules.ForumModule
import dagger.Subcomponent

@Subcomponent(modules = [ForumModule::class])
interface ForumComponent : BaseComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): ForumComponent
    }
}
