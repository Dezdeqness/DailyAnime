package com.dezdeqness.feature.details.anime.presentation.renderers

import androidx.compose.runtime.Composable
import com.dezdeqness.feature.details.anime.presentation.AnimeDetailsUiEvent
import com.dezdeqness.feature.details.anime.presentation.composables.sections.VideosSection
import com.dezdeqness.feature.details.anime.presentation.models.AnimeDetailsSection
import com.dezdeqness.feature.details.common.presentation.DetailsSectionRenderer

object VideosSectionRenderer :
    DetailsSectionRenderer<AnimeDetailsSection.Videos, AnimeDetailsUiEvent> {
    override val rendererType: String = AnimeDetailsSection.Videos.TYPE

    @Composable
    override fun Render(
        section: AnimeDetailsSection.Videos,
        onEvent: (AnimeDetailsUiEvent) -> Unit,
    ) {
        VideosSection(
            items = section.items,
            onItemClick = { onEvent(AnimeDetailsUiEvent.VideoClicked(it)) },
        )
    }
}
