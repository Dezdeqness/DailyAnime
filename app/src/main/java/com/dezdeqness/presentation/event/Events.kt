package com.dezdeqness.presentation.event

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

data class OpenAnimeDetails(val animeId: Long, val title: String) : Event()

data class OpenVideo(val url: String) : ConsumableEvent()

data class NavigateToFilter(val filters: List<SearchSectionUiModel> = listOf()) : Event()

data class ApplyFilter(val filters: List<SearchSectionUiModel> = listOf()) : Event()

data object LanguageDisclaimer : Event()

data object NavigateToOnboarding : Event()

data object OpenCalendarTab : Event()

data object OpenHistoryPage : Event()

object NavigateToStats : Event()

object NavigateToSettings : Event()

object NavigateToHistory : Event()

data class NavigateToAchievements(val usedId: Long) : Event()

data class NavigateToFavourites(val usedId: Long) : Event()

object NavigateToLoginPage : Event()

object NavigateToSignUp : Event()

data class ShareUrl(val url: String) : ConsumableEvent()

object CloseAuthorization : Event()

object AuthorizationSuccess : Event()

object NavigateToMainFlow : Event()

data class AuthUrl(val url: String) : Event()

data object HandlePermission : Event()
