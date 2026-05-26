package com.dezdeqness.feature.details.person.presentation.store

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
            )

            is PersonDetailsNamespace.Event.OnDetailsLoaded -> {
                state {
                    state.copy(
                        status = DetailsStatus.Loaded,
                        title = event.title,
                        shareUrl = event.shareUrl,
                        sections = event.sections,
                    )
                }
            }
        }
    }
}
