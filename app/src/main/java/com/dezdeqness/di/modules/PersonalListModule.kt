package com.dezdeqness.di.modules

import androidx.lifecycle.ViewModel
import com.dezdeqness.core.di.AssistedViewModelFactory
import com.dezdeqness.core.di.AssistedViewModelKey
import com.dezdeqness.core.di.ViewModelKey
import com.dezdeqness.feature.personallist.PersonalListTabViewModel
import com.dezdeqness.feature.personallist.PersonalListViewModel
import com.dezdeqness.presentation.features.unauthorized.host.PersonalListHostViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(includes = [PersonalModule::class])
abstract class PersonalListModule {

    @Binds
    @IntoMap
    @ViewModelKey(PersonalListViewModel::class)
    abstract fun bindPersonalListViewModel(viewModel: PersonalListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PersonalListHostViewModel::class)
    abstract fun bindPersonalListHostViewModel(viewModel: PersonalListHostViewModel): ViewModel

    @Binds
    @IntoMap
    @AssistedViewModelKey(PersonalListTabViewModel::class)
    abstract fun bindPersonalListTabViewModelFactory(
        factory: PersonalListTabViewModel.Factory
    ): AssistedViewModelFactory<out ViewModel>

}
