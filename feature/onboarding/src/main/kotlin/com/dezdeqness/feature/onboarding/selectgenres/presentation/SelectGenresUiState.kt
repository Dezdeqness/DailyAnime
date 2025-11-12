package com.dezdeqness.feature.onboarding.selectgenres.presentation

import androidx.compose.runtime.Immutable
import com.dezdeqness.feature.onboarding.selectgenres.presentation.models.GenreUiModel

@Immutable
data class SelectGenresUiState(
    val genres: List<GenreUiModel>,
    val selectedGenres: Set<String> = emptySet(),
)
