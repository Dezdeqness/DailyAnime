package com.dezdeqness.feature.auth.di

import androidx.lifecycle.ViewModel
import com.dezdeqness.contract.auth.SessionManager
import com.dezdeqness.feature.auth.data.SessionManagerImpl
import com.dezdeqness.feature.auth.presentation.AuthorizationViewModel
import com.dezdeqness.foundation.di.SourceScope
import com.dezdeqness.foundation.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AuthModule {

    @Binds
    @SourceScope
    internal abstract fun bindSessionManager(impl: SessionManagerImpl): SessionManager

    @Binds
    @IntoMap
    @ViewModelKey(AuthorizationViewModel::class)
    internal abstract fun bindAuthorizationViewModel(viewModel: AuthorizationViewModel): ViewModel
}
