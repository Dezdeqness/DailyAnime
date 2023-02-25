package com.dezdeqness.di.modules

import androidx.lifecycle.ViewModel
import com.dezdeqness.di.ViewModelKey
import com.dezdeqness.presentation.features.unauthorized.UnauthorizedViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class UnauthorizedModule {

    @Binds
    @IntoMap
    @ViewModelKey(UnauthorizedViewModel::class)
    abstract fun bindUnauthorizedViewModel(viewModel: UnauthorizedViewModel): ViewModel

}
