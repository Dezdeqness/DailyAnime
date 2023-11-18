package com.dezdeqness.di.modules

import androidx.lifecycle.ViewModel
import com.dezdeqness.di.ViewModelKey
import com.dezdeqness.presentation.features.authorization.AuthorizationViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(includes = [])
abstract class AuthorizationModule {

    @Binds
    @IntoMap
    @ViewModelKey(AuthorizationViewModel::class)
    abstract fun bindAuthorizationViewModel(viewModel: AuthorizationViewModel): ViewModel
}
