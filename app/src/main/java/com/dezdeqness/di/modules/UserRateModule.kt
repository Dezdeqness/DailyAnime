package com.dezdeqness.di.modules

import androidx.lifecycle.ViewModel
import com.dezdeqness.di.ViewModelKey
import com.dezdeqness.di.subcomponents.UserRateModule
import com.dezdeqness.presentation.features.userrate.UserRateViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(includes = [PersonalModule::class, UserRateModule::class])
abstract class UserRateModule {

    @Binds
    @IntoMap
    @ViewModelKey(UserRateViewModel::class)
    abstract fun bindEditRateViewModel(viewModel: UserRateViewModel): ViewModel

}
