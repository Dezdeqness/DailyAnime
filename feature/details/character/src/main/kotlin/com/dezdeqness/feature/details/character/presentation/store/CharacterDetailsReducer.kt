package com.dezdeqness.feature.details.character.presentation.store

import com.dezdeqness.contract.favourite.model.FavouriteButtonState
import com.dezdeqness.contract.favourite.model.FavouriteLinkedType
import com.dezdeqness.feature.details.common.presentation.store.BaseDetailsCommand
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
                onFavouriteButtonChanged = { state.copy(favouriteButton = it) },
                onFavouriteToggleClicked = {
                    val current = state.favouriteButton as? FavouriteButtonState.Idle ?: return@handleBaseDetailsEvent
                    val target = !current.isFavourite
                    state {
                        state.copy(favouriteButton = FavouriteButtonState.Processing(targetIsFavourite = target))
                    }
                    commands {
                        +CharacterDetailsNamespace.Command.Base(
                            BaseDetailsCommand.ToggleFavourite(
                                targetId = state.id,
                                type = FavouriteLinkedType.CHARACTER,
                            ),
                        )
                    }
                },
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
                if (event.isAuthorized) {
                    commands {
                        +CharacterDetailsNamespace.Command.Base(
                            BaseDetailsCommand.ObserveFavouriteStatus(
                                targetId = state.id,
                                type = FavouriteLinkedType.CHARACTER,
                            ),
                        )
                        +CharacterDetailsNamespace.Command.Base(
                            BaseDetailsCommand.FetchFavourites(),
                        )
                    }
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
