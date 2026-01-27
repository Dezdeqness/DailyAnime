package com.dezdeqness.di.modules

import androidx.lifecycle.ViewModel
import com.dezdeqness.core.di.AssistedViewModelFactory
import com.dezdeqness.core.di.AssistedViewModelKey
import com.dezdeqness.domain.repository.UserRatesRepository
import com.dezdeqness.domain.usecases.GetPersonalListByStatusUseCase
import com.dezdeqness.feature.personallist.tab.PersonalListViewModel
import com.dezdeqness.feature.personallist.tab.store.PersonalListActor
import com.dezdeqness.feature.personallist.tab.store.PersonalListNamespace
import com.dezdeqness.feature.personallist.tab.store.personalListReducer
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import money.vivid.elmslie.core.store.ElmStore

@Module(includes = [PersonalModule::class])
abstract class PersonalListTabModule {

    companion object {
        @Provides
        fun provideGetPersonalListByStatusUseCase(userRatesRepository: UserRatesRepository) =
            GetPersonalListByStatusUseCase(userRatesRepository = userRatesRepository)

        @Provides
        fun providePersonalListStore(actor: PersonalListActor): ElmStore<
                PersonalListNamespace.Event,
                PersonalListNamespace.State,
                PersonalListNamespace.Effect,
                PersonalListNamespace.Command> =
            ElmStore(
                initialState = PersonalListNamespace.State(),
                reducer = personalListReducer,
                actor = actor,
            )
    }

    @Binds
    @IntoMap
    @AssistedViewModelKey(PersonalListViewModel::class)
    abstract fun bindPersonalListViewModelFactory(
        factory: PersonalListViewModel.Factory
    ): AssistedViewModelFactory<out ViewModel>
}
