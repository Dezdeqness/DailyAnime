package com.dezdeqness.presentation.event

import com.dezdeqness.presentation.features.details.AnimeStatsTransferModel
import com.dezdeqness.presentation.features.userrate.EditRateUiModel
import com.dezdeqness.presentation.models.SearchSectionUiModel
import java.util.UUID

sealed class Event {
    val id: String = UUID.randomUUID().toString()
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Event

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

}

sealed class ConsumableEvent : Event()

data class AnimeDetails(val animeId: Long, val title: String) : Event()

data class OpenVideo(val url: String) : ConsumableEvent()

data class NavigateToFilter(val filters: List<SearchSectionUiModel> = listOf()) : Event()

data class ApplyFilter(val filters: List<SearchSectionUiModel> = listOf()) : Event()

data class NavigateToEditRate(
    val rateId: Long,
    val title: String,
) : Event()

data object LanguageDisclaimer : Event()

data object OpenCalendarTab : Event()

data class EditUserRate(val userRateUiModel: EditRateUiModel) : Event()

data class NavigateToScreenshotViewer(val currentIndex: Int, val screenshots: List<String>) : Event()

object NavigateToStats : Event()

object NavigateToSettings : Event()

object NavigateToHistory : Event()

object NavigateToLoginPage : Event()

object NavigateToSignUp : Event()

data class SwitchDarkTheme(val isEnabled: Boolean) : Event()

data class ShareUrl(val url: String) : ConsumableEvent()

data class NavigateToAnimeStats(
    val scoreList: List<AnimeStatsTransferModel>,
    val statusesList: List<AnimeStatsTransferModel>,
) : Event()

data class NavigateToSimilar(val animeId: Long) : Event()

data class NavigateToChronology(val animeId: Long) : Event()

data class NavigateToCharacterDetails(val characterId: Long) : Event()

object CloseAuthorization: Event()

object AuthorizationSuccess : Event()

object NavigateToMainFlow : Event()

data class AuthUrl(val url: String) : Event()

data object HandlePermission : Event()
