package com.dezdeqness.di.modules

import androidx.lifecycle.ViewModel
import com.dezdeqness.core.di.ViewModelKey
import com.dezdeqness.domain.repository.UserRatesRepository
import com.dezdeqness.domain.usecases.SearchPersonalListUseCase
import com.dezdeqness.feature.personallist.search.PersonalListSearchViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module(includes = [PersonalModule::class])
abstract class PersonalListSearchModule {

    companion object {
        @Provides
        fun provideSearchPersonalListUseCase(userRatesRepository: UserRatesRepository) =
            SearchPersonalListUseCase(userRatesRepository = userRatesRepository)
    }

    @Binds
    @IntoMap
    @ViewModelKey(PersonalListSearchViewModel::class)
    abstract fun bindPersonalListSearchViewModel(viewModel: PersonalListSearchViewModel): ViewModel
}
