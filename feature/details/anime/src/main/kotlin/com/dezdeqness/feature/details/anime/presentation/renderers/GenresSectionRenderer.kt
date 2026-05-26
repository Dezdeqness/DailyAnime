package com.dezdeqness.feature.details.anime.presentation.renderers

import androidx.compose.runtime.Composable
import com.dezdeqness.feature.details.anime.presentation.AnimeDetailsUiEvent
import com.dezdeqness.feature.details.anime.presentation.composables.sections.GenresSection
import com.dezdeqness.feature.details.anime.presentation.models.AnimeDetailsSection
import com.dezdeqness.feature.details.common.presentation.DetailsSectionRenderer

object GenresSectionRenderer :
    DetailsSectionRenderer<AnimeDetailsSection.Genres, AnimeDetailsUiEvent> {
    override val rendererType: String = AnimeDetailsSection.Genres.TYPE

    @Composable
    override fun Render(
        section: AnimeDetailsSection.Genres,
        onEvent: (AnimeDetailsUiEvent) -> Unit,
    ) {
        GenresSection(items = section.items)
    }
}
