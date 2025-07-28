package com.dezdeqness.di.subcomponents

import com.dezdeqness.di.core.BaseComponent
import com.dezdeqness.di.modules.CalendarListModule
import dagger.Subcomponent

@Subcomponent(modules = [CalendarListModule::class])
interface CalendarComponent : BaseComponent{

    @Subcomponent.Factory
    interface Factory {
        fun create(): CalendarComponent
    }
}
