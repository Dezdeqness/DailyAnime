package com.dezdeqness.di.modules

import androidx.lifecycle.ViewModel
import com.dezdeqness.core.di.ViewModelKey
import com.dezdeqness.feature.personallist.search.PersonalListSearchViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(includes = [PersonalModule::class])
abstract class PersonalListSearchModule {

    @Binds
    @IntoMap
    @ViewModelKey(PersonalListSearchViewModel::class)
    abstract fun bindPersonalListSearchViewModel(viewModel: PersonalListSearchViewModel): ViewModel
}
