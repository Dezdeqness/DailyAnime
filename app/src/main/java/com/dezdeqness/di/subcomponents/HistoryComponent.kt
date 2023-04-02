package com.dezdeqness.di.subcomponents

import com.dezdeqness.di.modules.HistoryModule
import com.dezdeqness.presentation.features.history.HistoryFragment
import dagger.Subcomponent

@Subcomponent(modules = [HistoryModule::class])
interface HistoryComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): HistoryComponent
    }

    fun inject(fragment: HistoryFragment)
}
