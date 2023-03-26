package com.dezdeqness.di.modules

import androidx.lifecycle.ViewModel
import com.dezdeqness.di.ViewModelKey
import com.dezdeqness.presentation.features.unauthorized.host.UnathorizedHostViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class UnauthorizedHostModule {

    @Binds
    @IntoMap
    @ViewModelKey(UnathorizedHostViewModel::class)
    abstract fun bindUnauthorizedHostViewModel(viewModel: UnathorizedHostViewModel): ViewModel

}
