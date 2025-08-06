package com.dezdeqness.feature.achievements.presentation

import com.dezdeqness.feature.achievements.presentation.models.AchievementsUiModel

data class AchievementsUiState(
    val status: Status = Status.Initial,
    val common: List<AchievementsUiModel> = listOf(),
    val genres: List<AchievementsUiModel> = listOf(),
)

enum class Status {
    Initial,
    Loading,
    Empty,
    Error,
    Loaded,
}
