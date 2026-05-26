package com.dezdeqness.feature.details.person.presentation

import androidx.lifecycle.viewmodel.CreationExtras
import com.dezdeqness.feature.details.common.presentation.DetailsViewModel
import com.dezdeqness.feature.details.common.presentation.store.BaseDetailsEvent
import com.dezdeqness.feature.details.person.presentation.store.PersonDetailsNamespace
import com.dezdeqness.foundation.di.AssistedViewModelFactory
import money.vivid.elmslie.core.store.ElmStore
import javax.inject.Inject

object PersonIdKey : CreationExtras.Key<Long>

class PersonDetailsViewModel(
    store: ElmStore<PersonDetailsNamespace.Event, PersonDetailsNamespace.State, PersonDetailsNamespace.Effect, PersonDetailsNamespace.Command>,
    translator: PersonDetailsEventTranslator,
    personId: Long,
) : DetailsViewModel<PersonDetailsUiEvent, PersonDetailsNamespace.Event, PersonDetailsNamespace.State, PersonDetailsNamespace.Effect, PersonDetailsNamespace.Command>(
    store = store,
    initialState = PersonDetailsNamespace.State(id = personId),
    initialEvent = PersonDetailsNamespace.Event.Base(BaseDetailsEvent.InitialLoad(personId)),
    translator = translator,
) {

    class Factory @Inject constructor(
        private val store: ElmStore<
                PersonDetailsNamespace.Event,
                PersonDetailsNamespace.State,
                PersonDetailsNamespace.Effect,
                PersonDetailsNamespace.Command>,
        private val translator: PersonDetailsEventTranslator,
    ) : AssistedViewModelFactory<PersonDetailsViewModel> {

        override fun create(extras: CreationExtras): PersonDetailsViewModel {
            val personId = extras[PersonIdKey] ?: 0L
            return PersonDetailsViewModel(
                store = store,
                translator = translator,
                personId = personId,
            )
        }
    }
}
