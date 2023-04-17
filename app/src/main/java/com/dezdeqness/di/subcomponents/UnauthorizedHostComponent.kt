package com.dezdeqness.di.subcomponents

import com.dezdeqness.di.modules.UnauthorizedHostModule
import com.dezdeqness.presentation.features.unauthorized.host.BaseUnauthorizedHostFragment
import dagger.Subcomponent


@Subcomponent(modules = [UnauthorizedHostModule::class])
interface UnauthorizedHostComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): UnauthorizedHostComponent
    }

    fun inject(fragment: BaseUnauthorizedHostFragment)

}
