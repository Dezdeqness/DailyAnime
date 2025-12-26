package com.dezdeqness.di.modules

import androidx.lifecycle.ViewModel
import com.dezdeqness.core.di.ViewModelKey
import com.dezdeqness.feature.userrate.UserRateViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(includes = [PersonalModule::class])
abstract class UserRateModule {

    @Binds
    @IntoMap
    @ViewModelKey(UserRateViewModel::class)
    abstract fun bindEditRateViewModel(viewModel: UserRateViewModel): ViewModel

}
