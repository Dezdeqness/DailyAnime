package com.dezdeqness.feature.details.character.presentation.store

import com.dezdeqness.feature.details.common.presentation.store.BaseDetailsReducer
import com.dezdeqness.feature.details.common.presentation.store.DetailsStatus

val characterDetailsReducer = object : BaseDetailsReducer<
        CharacterDetailsNamespace.Event,
        CharacterDetailsNamespace.State,
        CharacterDetailsNamespace.Effect,
        CharacterDetailsNamespace.Command,
        >(
    wrapCommand = CharacterDetailsNamespace.Command::Base,
    wrapEffect = CharacterDetailsNamespace.Effect::Base,
) {
    override fun Result.reduce(event: CharacterDetailsNamespace.Event) {
        when (event) {
            is CharacterDetailsNamespace.Event.Base -> handleBaseDetailsEvent(
                event = event.event,
                onInitialLoad = { id ->
                    CharacterDetailsNamespace.State(
                        id = id,
                        status = DetailsStatus.Loading,
                    )
                },
                onLoading = { state.copy(status = DetailsStatus.Loading) },
                onError = { state.copy(status = DetailsStatus.Error) },
            )

            is CharacterDetailsNamespace.Event.OnDetailsLoaded -> {
                state {
                    state.copy(
                        status = DetailsStatus.Loaded,
                        title = event.title,
                        shareUrl = event.shareUrl,
                        sections = event.sections,
                    )
                }
            }

            is CharacterDetailsNamespace.Event.SeyuClicked -> {
                effects { +CharacterDetailsNamespace.Effect.NavigateToPerson(event.personId) }
            }

            is CharacterDetailsNamespace.Event.AnimeClicked -> {
                effects { +CharacterDetailsNamespace.Effect.NavigateToAnime(event.animeId) }
            }
        }
    }
}
