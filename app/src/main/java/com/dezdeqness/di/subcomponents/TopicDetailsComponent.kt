package com.dezdeqness.di.subcomponents

import com.dezdeqness.di.core.BaseComponent
import com.dezdeqness.di.modules.TopicDetailsModule
import dagger.Subcomponent

@Subcomponent(modules = [TopicDetailsModule::class])
interface TopicDetailsComponent : BaseComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): TopicDetailsComponent
    }
}
