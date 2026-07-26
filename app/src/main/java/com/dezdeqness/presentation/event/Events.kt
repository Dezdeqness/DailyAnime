package com.dezdeqness.presentation.event

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

object NavigateToMainFlow : Event()

data object HandlePermission : Event()
