package com.dezdeqness.di.modules

import androidx.lifecycle.ViewModel
import com.dezdeqness.di.ViewModelKey
import com.dezdeqness.di.subcomponents.UserRateModule
import com.dezdeqness.presentation.features.editrate.EditRateViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(includes = [PersonalModule::class, UserRateModule::class])
abstract class EditRateModule {

    @Binds
    @IntoMap
    @ViewModelKey(EditRateViewModel::class)
    abstract fun bindEditRateViewModel(viewModel: EditRateViewModel): ViewModel

}
