package com.dezdeqness.di.subcomponents

import com.dezdeqness.di.core.BaseComponent
import com.dezdeqness.di.modules.HistoryModule
import dagger.Subcomponent

@Subcomponent(modules = [HistoryModule::class])
interface HistoryComponent : BaseComponent{
    @Subcomponent.Factory
    interface Factory {
        fun create(): HistoryComponent
    }
}
