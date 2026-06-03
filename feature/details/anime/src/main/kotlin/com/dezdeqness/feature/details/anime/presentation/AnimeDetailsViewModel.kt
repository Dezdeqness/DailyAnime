package com.dezdeqness.feature.details.anime.presentation

import androidx.lifecycle.viewmodel.CreationExtras
import com.dezdeqness.feature.details.anime.presentation.store.AnimeDetailsNamespace
import com.dezdeqness.feature.details.common.presentation.DetailsViewModel
import com.dezdeqness.feature.details.common.presentation.store.BaseDetailsEvent
import com.dezdeqness.foundation.di.AssistedViewModelFactory
import javax.inject.Inject
import money.vivid.elmslie.core.store.ElmStore

object AnimeIdKey : CreationExtras.Key<Long>

class AnimeDetailsViewModel(
    store: ElmStore<
        AnimeDetailsNamespace.Event,
        AnimeDetailsNamespace.State,
        AnimeDetailsNamespace.Effect,
        AnimeDetailsNamespace.Command,
        >,
    translator: AnimeDetailsEventTranslator,
    animeId: Long,
) : DetailsViewModel<
    AnimeDetailsUiEvent,
    AnimeDetailsNamespace.Event,
    AnimeDetailsNamespace.State,
    AnimeDetailsNamespace.Effect,
    AnimeDetailsNamespace.Command,
    >(
    store = store,
    initialState = AnimeDetailsNamespace.State(id = animeId),
    initialEvent = AnimeDetailsNamespace.Event.Base(BaseDetailsEvent.InitialLoad(animeId)),
    translator = translator,
) {

    class Factory @Inject constructor(
        private val store: ElmStore<
            AnimeDetailsNamespace.Event,
            AnimeDetailsNamespace.State,
            AnimeDetailsNamespace.Effect,
            AnimeDetailsNamespace.Command,
            >,
        private val translator: AnimeDetailsEventTranslator,
    ) : AssistedViewModelFactory<AnimeDetailsViewModel> {

        override fun create(extras: CreationExtras): AnimeDetailsViewModel {
            val animeId = extras[AnimeIdKey] ?: 0L
            return AnimeDetailsViewModel(
                store = store,
                translator = translator,
                animeId = animeId,
            )
        }
    }
}
