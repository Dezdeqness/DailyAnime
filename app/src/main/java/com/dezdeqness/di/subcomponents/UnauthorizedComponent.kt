package com.dezdeqness.di.subcomponents

import com.dezdeqness.di.modules.UnauthorizedModule
import com.dezdeqness.presentation.features.unauthorized.UnauthorizedFragment
import dagger.Subcomponent


@Subcomponent(modules = [UnauthorizedModule::class])
interface UnauthorizedComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): UnauthorizedComponent
    }

    fun inject(fragment: UnauthorizedFragment)

}
