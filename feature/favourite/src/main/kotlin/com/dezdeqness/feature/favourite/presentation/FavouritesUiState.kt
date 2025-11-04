package com.dezdeqness.feature.favourite.presentation

import androidx.compose.runtime.Immutable
import com.dezdeqness.feature.favourite.presentation.models.FavouritesUiModel

@Immutable
data class FavouritesUiState(
    val status: Status = Status.Initial,
    val items: List<FavouritesUiModel> = listOf(),
)

enum class Status {
    Initial,
    Loading,
    Empty,
    Error,
    Loaded,
}
