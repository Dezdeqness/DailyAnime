package com.dezdeqness.di.modules

import androidx.lifecycle.ViewModel
import com.dezdeqness.feature.calendar.presentation.CalendarViewModel
import com.dezdeqness.foundation.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(includes = [CalendarModule::class])
abstract class CalendarListModule {

    @Binds
    @IntoMap
    @ViewModelKey(CalendarViewModel::class)
    abstract fun bindViewModel(viewModel: CalendarViewModel): ViewModel
}
