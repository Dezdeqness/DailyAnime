package com.dezdeqness.di.modules

import androidx.lifecycle.ViewModel
import com.dezdeqness.core.di.ViewModelKey
import com.dezdeqness.feature.personallist.PersonalListTabsViewModel
import com.dezdeqness.presentation.features.unauthorized.host.PersonalListHostViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(includes = [PersonalModule::class])
abstract class PersonalListModule {

    @Binds
    @IntoMap
    @ViewModelKey(PersonalListTabsViewModel::class)
    abstract fun bindPersonalListTabsViewModel(viewModel: PersonalListTabsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PersonalListHostViewModel::class)
    abstract fun bindPersonalListHostViewModel(viewModel: PersonalListHostViewModel): ViewModel

}
