package com.dezdeqness.di.modules

import androidx.lifecycle.ViewModel
import com.dezdeqness.feature.details.character.presentation.CharacterDetailsViewModel
import com.dezdeqness.feature.details.character.presentation.store.CharacterDetailsActor
import com.dezdeqness.feature.details.character.presentation.store.CharacterDetailsNamespace.Command
import com.dezdeqness.feature.details.character.presentation.store.CharacterDetailsNamespace.Effect
import com.dezdeqness.feature.details.character.presentation.store.CharacterDetailsNamespace.Event
import com.dezdeqness.feature.details.character.presentation.store.CharacterDetailsNamespace.State
import com.dezdeqness.feature.details.character.presentation.store.characterDetailsReducer
import com.dezdeqness.foundation.di.AssistedViewModelFactory
import com.dezdeqness.foundation.di.AssistedViewModelKey
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import money.vivid.elmslie.core.store.ElmStore

@Module(includes = [CharacterModule::class])
abstract class CharacterDetailsFeatureModule {

    companion object {
        @Provides
        fun provideCharacterDetailsStore(
            actor: CharacterDetailsActor,
        ): ElmStore<Event, State, Effect, Command> =
            ElmStore(
                initialState = State(),
                reducer = characterDetailsReducer,
                actor = actor,
            )
    }

    @Binds
    @IntoMap
    @AssistedViewModelKey(CharacterDetailsViewModel::class)
    abstract fun bindCharacterDetailsViewModelFactory(
        factory: CharacterDetailsViewModel.Factory,
    ): AssistedViewModelFactory<out ViewModel>
}
