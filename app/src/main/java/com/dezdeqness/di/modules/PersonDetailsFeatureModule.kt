package com.dezdeqness.di.modules

import androidx.lifecycle.ViewModel
import com.dezdeqness.feature.details.person.presentation.PersonDetailsViewModel
import com.dezdeqness.feature.details.person.presentation.store.PersonDetailsActor
import com.dezdeqness.feature.details.person.presentation.store.PersonDetailsNamespace.Command
import com.dezdeqness.feature.details.person.presentation.store.PersonDetailsNamespace.Effect
import com.dezdeqness.feature.details.person.presentation.store.PersonDetailsNamespace.Event
import com.dezdeqness.feature.details.person.presentation.store.PersonDetailsNamespace.State
import com.dezdeqness.feature.details.person.presentation.store.personDetailsReducer
import com.dezdeqness.foundation.di.AssistedViewModelFactory
import com.dezdeqness.foundation.di.AssistedViewModelKey
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import money.vivid.elmslie.core.store.ElmStore

@Module(includes = [PersonModule::class])
abstract class PersonDetailsFeatureModule {

    companion object {
        @Provides
        fun providePersonDetailsStore(actor: PersonDetailsActor): ElmStore<Event, State, Effect, Command> = ElmStore(
            initialState = State(),
            reducer = personDetailsReducer,
            actor = actor,
        )
    }

    @Binds
    @IntoMap
    @AssistedViewModelKey(PersonDetailsViewModel::class)
    abstract fun bindPersonDetailsViewModelFactory(
        factory: PersonDetailsViewModel.Factory,
    ): AssistedViewModelFactory<out ViewModel>
}
