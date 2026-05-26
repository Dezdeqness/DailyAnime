package com.dezdeqness.feature.details.anime.presentation.store

import com.dezdeqness.contract.anime.model.AnimeDetailsFullEntity
import com.dezdeqness.contract.anime.model.UserRateEntity
import com.dezdeqness.contract.user.model.StatsItemEntity
import com.dezdeqness.feature.details.common.presentation.DetailsSection
import com.dezdeqness.feature.details.common.presentation.store.BaseDetailsCommand
import com.dezdeqness.feature.details.common.presentation.store.BaseDetailsEffect
import com.dezdeqness.feature.details.common.presentation.store.BaseDetailsEvent
import com.dezdeqness.feature.details.common.presentation.store.DetailsState
import com.dezdeqness.feature.details.common.presentation.store.DetailsStatus
import com.dezdeqness.feature.userrate.EditRateUiModel

interface AnimeDetailsNamespace {

    data class State(
        override val id: Long = 0L,
        override val status: DetailsStatus = DetailsStatus.Initial,
        override val title: String = "",
        override val shareUrl: String = "",
        val isAuthorized: Boolean = false,
        val details: AnimeDetailsFullEntity? = null,
        override val sections: List<DetailsSection> = emptyList(),
        val editRateSheet: EditRateSheetState = EditRateSheetState.None,
    ) : DetailsState

    sealed interface Event {
        data class Base(val event: BaseDetailsEvent) : Event

        data class OnDetailsLoaded(
            val details: AnimeDetailsFullEntity,
            val sections: List<DetailsSection>,
            val isAuthorized: Boolean,
        ) : Event

        data object EditRateClicked : Event
        data object EditRateClosed : Event
        data class SaveUserRate(val model: EditRateUiModel) : Event
        data class OnUserRateSaved(val isCreate: Boolean, val rate: UserRateEntity) : Event
        data object OnUserRateSaveError : Event

        data object StatsClicked : Event
        data object SimilarClicked : Event
        data object ChronologyClicked : Event
        data class RelatedClicked(val animeId: Long) : Event
        data class CharacterClicked(val characterId: Long) : Event
        data class ScreenshotClicked(val previewUrl: String) : Event
        data class VideoClicked(val url: String) : Event
    }

    sealed interface Effect {
        data class Base(val effect: BaseDetailsEffect) : Effect
        data object EditRateError : Effect
        data class EditRateCreated(val rate: UserRateEntity) : Effect
        data class EditRateUpdated(val rate: UserRateEntity) : Effect
        data class NavigateToAnime(val animeId: Long) : Effect
        data class NavigateToCharacter(val characterId: Long) : Effect
        data class NavigateToSimilar(val animeId: Long) : Effect
        data class NavigateToChronology(val animeId: Long) : Effect
        data class NavigateToStats(
            val scores: List<StatsItemEntity>,
            val statuses: List<StatsItemEntity>,
        ) : Effect
        data class NavigateToScreenshotViewer(
            val index: Int,
            val urls: List<String>,
        ) : Effect
        data class OpenVideo(val url: String) : Effect
    }

    sealed interface Command {
        data class Base(val command: BaseDetailsCommand) : Command
        data class CreateOrUpdateUserRate(
            val animeId: Long,
            val model: EditRateUiModel,
        ) : Command
    }
}

sealed interface EditRateSheetState {
    data object None : EditRateSheetState
    data class Visible(val userRateId: Long, val title: String) : EditRateSheetState
}
