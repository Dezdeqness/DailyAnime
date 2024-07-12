package com.dezdeqness.di.modules

import androidx.lifecycle.ViewModel
import com.dezdeqness.di.ViewModelKey
import com.dezdeqness.presentation.features.unauthorized.host.PersonalListHostViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class PersonalListHostModule {

    @Binds
    @IntoMap
    @ViewModelKey(PersonalListHostViewModel::class)
    abstract fun bindPersonalListHostViewModel(viewModel: PersonalListHostViewModel): ViewModel

}
