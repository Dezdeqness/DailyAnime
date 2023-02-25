package com.dezdeqness.di.modules

import androidx.lifecycle.ViewModel
import com.dezdeqness.di.ViewModelKey
import com.dezdeqness.presentation.features.personallist.list.PersonalListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(includes = [PersonalModule::class])
abstract class PersonalListModule {

    @Binds
    @IntoMap
    @ViewModelKey(PersonalListViewModel::class)
    abstract fun bindPersonalListViewModel(viewModel: PersonalListViewModel): ViewModel

}
