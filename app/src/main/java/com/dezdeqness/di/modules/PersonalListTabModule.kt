package com.dezdeqness.di.modules

import androidx.lifecycle.ViewModel
import com.dezdeqness.contract.userrate.usecases.GetPersonalListByStatusUseCase
import com.dezdeqness.domain.userrate.usecases.GetPersonalListByStatusUseCaseImpl
import com.dezdeqness.feature.personallist.tab.PersonalListViewModel
import com.dezdeqness.feature.personallist.tab.store.PersonalListActor
import com.dezdeqness.feature.personallist.tab.store.PersonalListNamespace
import com.dezdeqness.feature.personallist.tab.store.personalListReducer
import com.dezdeqness.foundation.di.AssistedViewModelFactory
import com.dezdeqness.foundation.di.AssistedViewModelKey
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import money.vivid.elmslie.core.store.ElmStore

@Module(includes = [PersonalModule::class])
abstract class PersonalListTabModule {

    companion object {
        @Provides
        fun providePersonalListStore(
            actor: PersonalListActor,
        ): ElmStore<
            PersonalListNamespace.Event,
            PersonalListNamespace.State,
            PersonalListNamespace.Effect,
            PersonalListNamespace.Command,
            > =
            ElmStore(
                initialState = PersonalListNamespace.State(),
                reducer = personalListReducer,
                actor = actor,
            )
    }

    @Binds
    abstract fun bindGetPersonalListByStatusUseCase(
        getPersonalListByStatusUseCase: GetPersonalListByStatusUseCaseImpl,
    ): GetPersonalListByStatusUseCase

    @Binds
    @IntoMap
    @AssistedViewModelKey(PersonalListViewModel::class)
    abstract fun bindPersonalListViewModelFactory(
        factory: PersonalListViewModel.Factory,
    ): AssistedViewModelFactory<out ViewModel>
}
