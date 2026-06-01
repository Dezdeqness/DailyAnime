package com.dezdeqness.feature.details.person.presentation.store

import com.dezdeqness.contract.favourite.model.FavouriteButtonState
import com.dezdeqness.contract.favourite.model.FavouriteLinkedType
import com.dezdeqness.feature.details.common.presentation.store.BaseDetailsCommand
import com.dezdeqness.feature.details.common.presentation.store.BaseDetailsReducer
import com.dezdeqness.feature.details.common.presentation.store.DetailsStatus

val personDetailsReducer = object : BaseDetailsReducer<
        PersonDetailsNamespace.Event,
        PersonDetailsNamespace.State,
        PersonDetailsNamespace.Effect,
        PersonDetailsNamespace.Command,
        >(
    wrapCommand = PersonDetailsNamespace.Command::Base,
    wrapEffect = PersonDetailsNamespace.Effect::Base,
) {
    override fun Result.reduce(event: PersonDetailsNamespace.Event) {
        when (event) {
            is PersonDetailsNamespace.Event.Base -> handleBaseDetailsEvent(
                event = event.event,
                onInitialLoad = { id ->
                    PersonDetailsNamespace.State(
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
                        +PersonDetailsNamespace.Command.Base(
                            BaseDetailsCommand.ToggleFavourite(
                                targetId = state.id,
                                type = FavouriteLinkedType.PERSON,
                                kind = state.favouriteKind,
                            ),
                        )
                    }
                },
            )

            is PersonDetailsNamespace.Event.OnDetailsLoaded -> {
                state {
                    state.copy(
                        status = DetailsStatus.Loaded,
                        title = event.title,
                        shareUrl = event.shareUrl,
                        sections = event.sections,
                        favouriteKind = event.favouriteKind,
                    )
                }
                if (event.isAuthorized) {
                    commands {
                        +PersonDetailsNamespace.Command.Base(
                            BaseDetailsCommand.ObserveFavouriteStatus(
                                targetId = state.id,
                                type = FavouriteLinkedType.PERSON,
                            ),
                        )
                        +PersonDetailsNamespace.Command.Base(
                            BaseDetailsCommand.FetchFavourites(),
                        )
                    }
                }
            }
        }
    }
}
