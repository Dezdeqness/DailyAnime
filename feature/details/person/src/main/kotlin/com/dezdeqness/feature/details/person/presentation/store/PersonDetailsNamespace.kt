package com.dezdeqness.feature.details.person.presentation.store

import com.dezdeqness.contract.favourite.model.FavouriteButtonState
import com.dezdeqness.contract.favourite.model.FavouriteKind
import com.dezdeqness.feature.details.common.presentation.DetailsSection
import com.dezdeqness.feature.details.common.presentation.store.BaseDetailsCommand
import com.dezdeqness.feature.details.common.presentation.store.BaseDetailsEffect
import com.dezdeqness.feature.details.common.presentation.store.BaseDetailsEvent
import com.dezdeqness.feature.details.common.presentation.store.DetailsState
import com.dezdeqness.feature.details.common.presentation.store.DetailsStatus

interface PersonDetailsNamespace {

    data class State(
        override val id: Long = 0L,
        override val status: DetailsStatus = DetailsStatus.Initial,
        override val title: String = "",
        override val shareUrl: String = "",
        override val sections: List<DetailsSection> = emptyList(),
        override val favouriteButton: FavouriteButtonState = FavouriteButtonState.Hidden,
        val favouriteKind: FavouriteKind = FavouriteKind.PERSON,
    ) : DetailsState

    sealed interface Event {
        data class Base(val event: BaseDetailsEvent) : Event
        data class OnDetailsLoaded(
            val title: String,
            val shareUrl: String,
            val sections: List<DetailsSection>,
            val favouriteKind: FavouriteKind,
            val isAuthorized: Boolean,
        ) : Event
    }

    sealed interface Effect {
        data class Base(val effect: BaseDetailsEffect) : Effect
    }

    sealed interface Command {
        data class Base(val command: BaseDetailsCommand) : Command
    }
}
