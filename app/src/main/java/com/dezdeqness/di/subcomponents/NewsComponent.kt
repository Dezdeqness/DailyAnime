package com.dezdeqness.di.subcomponents

import com.dezdeqness.di.core.BaseComponent
import com.dezdeqness.di.modules.NewsModule
import dagger.Subcomponent

@Subcomponent(modules = [NewsModule::class])
interface NewsComponent : BaseComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): NewsComponent
    }
}
