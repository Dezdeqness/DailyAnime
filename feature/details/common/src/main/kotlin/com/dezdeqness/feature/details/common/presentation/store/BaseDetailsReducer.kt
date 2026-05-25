package com.dezdeqness.feature.details.common.presentation.store

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
        }
    }
}
