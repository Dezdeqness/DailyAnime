package com.dezdeqness.feature.details.common.presentation.store

import com.dezdeqness.contract.favourite.model.FavouriteButtonState
import money.vivid.elmslie.core.store.StateReducer

abstract class BaseDetailsReducer<
    Event : Any,
    State : DetailsState,
    Effect : Any,
    Command : Any,
>(
    private val wrapCommand: (BaseDetailsCommand) -> Command,
    private val wrapEffect: (BaseDetailsEffect) -> Effect,
) : StateReducer<Event, State, Effect, Command>() {

    protected fun Result.handleBaseDetailsEvent(
        event: BaseDetailsEvent,
        onInitialLoad: (id: Long) -> State,
        onLoading: () -> State,
        onError: () -> State,
        onFavouriteButtonChanged: (FavouriteButtonState) -> State,
        onFavouriteToggleClicked: Result.() -> Unit = {},
    ) {
        when (event) {
            is BaseDetailsEvent.InitialLoad -> {
                state { onInitialLoad(event.id) }
                commands { +wrapCommand(BaseDetailsCommand.LoadDetails(event.id)) }
            }

            is BaseDetailsEvent.RetryClicked -> {
                if (state.status == DetailsStatus.Loading) return
                state { onLoading() }
                commands { +wrapCommand(BaseDetailsCommand.LoadDetails(state.id)) }
            }

            is BaseDetailsEvent.OnDetailsLoadError -> {
                state { onError() }
                effects { +wrapEffect(BaseDetailsEffect.Error) }
            }

            is BaseDetailsEvent.SharePressed -> {
                val url = state.shareUrl.takeIf { it.isNotEmpty() } ?: return
                effects { +wrapEffect(BaseDetailsEffect.Share(url)) }
            }

            is BaseDetailsEvent.FavouriteStatusChanged -> {
                state { onFavouriteButtonChanged(event.state) }
            }

            is BaseDetailsEvent.FavouriteToggleSucceeded -> Unit

            is BaseDetailsEvent.FavouriteToggleFailed -> {
                val previous = (state.favouriteButton as? FavouriteButtonState.Processing)
                    ?.let { FavouriteButtonState.Idle(isFavourite = !it.targetIsFavourite) }
                if (previous != null) {
                    state { onFavouriteButtonChanged(previous) }
                }
                effects { +wrapEffect(BaseDetailsEffect.FavouriteActionFailed) }
            }

            is BaseDetailsEvent.FavouriteToggleClicked -> onFavouriteToggleClicked()
        }
    }
}
