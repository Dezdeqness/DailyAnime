package com.dezdeqness.di.subcomponents

import com.dezdeqness.di.modules.CalendarListModule
import com.dezdeqness.presentation.features.calendar.CalendarFragment
import dagger.Subcomponent

@Subcomponent(modules = [CalendarListModule::class])
interface CalendarComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): CalendarComponent
    }

    fun inject(fragment: CalendarFragment)

}
