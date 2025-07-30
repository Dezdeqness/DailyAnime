package com.dezdeqness.presentation

import kotlinx.serialization.Serializable

@Serializable
object RootRoute

@Serializable
sealed interface BottomBarNav {
    @Serializable
    object PersonalList : BottomBarNav

    @Serializable
    object Home : BottomBarNav

    @Serializable
    object Calendar : BottomBarNav

    @Serializable
    object Search : BottomBarNav

    @Serializable
    object Profile : BottomBarNav
}

@Serializable
object History

@Serializable
object Settings

@Serializable
object Stats

@Serializable
data class Details(
    val id: Long,
    val isAnime: Boolean = true,
)

@Serializable
data class DetailsStats(
    val scoreString: String,
    val statusesString: String,
)

@Serializable
data class Similar(
    val id: Long,
)

@Serializable
data class Chronology(
    val id: Long,
)
