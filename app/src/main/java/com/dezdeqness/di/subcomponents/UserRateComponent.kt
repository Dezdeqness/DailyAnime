package com.dezdeqness.di.subcomponents

import com.dezdeqness.di.core.BaseComponent
import com.dezdeqness.di.modules.UserRateModule
import dagger.Subcomponent

@Subcomponent(modules = [UserRateModule::class])
interface UserRateComponent : BaseComponent {

    @Subcomponent.Builder
    interface Builder {
        fun build(): UserRateComponent
    }
}
