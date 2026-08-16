package com.dezdeqness.di.modules

import androidx.lifecycle.ViewModel
import com.dezdeqness.contract.userrate.usecases.SearchPersonalListUseCase
import com.dezdeqness.domain.userrate.usecases.SearchPersonalListUseCaseImpl
import com.dezdeqness.feature.personallist.search.PersonalListSearchViewModel
import com.dezdeqness.foundation.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(includes = [PersonalModule::class])
abstract class PersonalListSearchModule {

    @Binds
    abstract fun bindSearchPersonalListUseCase(
        searchPersonalListUseCase: SearchPersonalListUseCaseImpl,
    ): SearchPersonalListUseCase

    @Binds
    @IntoMap
    @ViewModelKey(PersonalListSearchViewModel::class)
    abstract fun bindPersonalListSearchViewModel(viewModel: PersonalListSearchViewModel): ViewModel
}
