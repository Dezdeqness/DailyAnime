package com.dezdeqness.feature.details.character.presentation

import androidx.lifecycle.viewmodel.CreationExtras
import com.dezdeqness.feature.details.character.presentation.store.CharacterDetailsNamespace
import com.dezdeqness.feature.details.common.presentation.DetailsViewModel
import com.dezdeqness.feature.details.common.presentation.store.BaseDetailsEvent
import com.dezdeqness.foundation.di.AssistedViewModelFactory
import javax.inject.Inject
import money.vivid.elmslie.core.store.ElmStore

object CharacterIdKey : CreationExtras.Key<Long>

class CharacterDetailsViewModel(
    store: ElmStore<
        CharacterDetailsNamespace.Event,
        CharacterDetailsNamespace.State,
        CharacterDetailsNamespace.Effect,
        CharacterDetailsNamespace.Command,
        >,
    translator: CharacterDetailsEventTranslator,
    characterId: Long,
) : DetailsViewModel<
    CharacterDetailsUiEvent,
    CharacterDetailsNamespace.Event,
    CharacterDetailsNamespace.State,
    CharacterDetailsNamespace.Effect,
    CharacterDetailsNamespace.Command,
    >(
    store = store,
    initialState = CharacterDetailsNamespace.State(id = characterId),
    initialEvent = CharacterDetailsNamespace.Event.Base(BaseDetailsEvent.InitialLoad(characterId)),
    translator = translator,
) {

    class Factory @Inject constructor(
        private val store: ElmStore<
            CharacterDetailsNamespace.Event,
            CharacterDetailsNamespace.State,
            CharacterDetailsNamespace.Effect,
            CharacterDetailsNamespace.Command,
            >,
        private val translator: CharacterDetailsEventTranslator,
    ) : AssistedViewModelFactory<CharacterDetailsViewModel> {

        override fun create(extras: CreationExtras): CharacterDetailsViewModel {
            val characterId = extras[CharacterIdKey] ?: 0L
            return CharacterDetailsViewModel(
                store = store,
                translator = translator,
                characterId = characterId,
            )
        }
    }
}
