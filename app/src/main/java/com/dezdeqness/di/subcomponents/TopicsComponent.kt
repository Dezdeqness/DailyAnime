package com.dezdeqness.di.subcomponents

import com.dezdeqness.di.core.BaseComponent
import com.dezdeqness.di.modules.TopicsArgsModule
import com.dezdeqness.di.modules.TopicsModule
import dagger.Subcomponent

@Subcomponent(modules = [TopicsModule::class, TopicsArgsModule::class])
interface TopicsComponent : BaseComponent {
    @Subcomponent.Builder
    interface Builder {

        fun argsModule(module: TopicsArgsModule): Builder

        fun build(): TopicsComponent
    }
}
