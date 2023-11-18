package com.dezdeqness.di.subcomponents

import com.dezdeqness.di.modules.AuthorizationModule
import com.dezdeqness.presentation.features.authorization.AuthorizationActivity
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Named

@Subcomponent(modules = [AuthorizationModule::class, AuthorizationArgsModule::class])
interface AuthorizationComponent {

    @Subcomponent.Builder
    interface Builder {

        fun argsModule(module: AuthorizationArgsModule): Builder

        fun build(): AuthorizationComponent
    }

    fun inject(activity: AuthorizationActivity)

}

@Module
class AuthorizationArgsModule(private val isLogin: Boolean) {

    @Named("isLogin")
    @Provides
    fun provideIsLogin() = isLogin

}
