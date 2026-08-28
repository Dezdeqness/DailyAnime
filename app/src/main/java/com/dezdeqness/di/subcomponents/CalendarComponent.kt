package com.dezdeqness.di.subcomponents

import com.dezdeqness.di.core.BaseComponent
import com.dezdeqness.feature.calendar.di.CalendarModule
import dagger.Subcomponent

@Subcomponent(modules = [CalendarModule::class])
interface CalendarComponent : BaseComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): CalendarComponent
    }
}
