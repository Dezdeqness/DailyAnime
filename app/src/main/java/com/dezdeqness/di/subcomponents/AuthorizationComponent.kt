package com.dezdeqness.di.subcomponents

import com.dezdeqness.presentation.features.authorization.AuthorizationActivity
import dagger.Subcomponent

@Subcomponent
interface AuthorizationComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): AuthorizationComponent
    }

    fun inject(activity: AuthorizationActivity)

}
