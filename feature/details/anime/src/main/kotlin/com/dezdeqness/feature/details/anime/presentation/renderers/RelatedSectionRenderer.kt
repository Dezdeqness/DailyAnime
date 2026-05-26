package com.dezdeqness.feature.details.anime.presentation.renderers

import androidx.compose.runtime.Composable
import com.dezdeqness.feature.details.anime.presentation.AnimeDetailsUiEvent
import com.dezdeqness.feature.details.anime.presentation.composables.sections.RelatedSection
import com.dezdeqness.feature.details.anime.presentation.models.AnimeDetailsSection
import com.dezdeqness.feature.details.common.presentation.DetailsSectionRenderer

object RelatedSectionRenderer :
    DetailsSectionRenderer<AnimeDetailsSection.Related, AnimeDetailsUiEvent> {
    override val rendererType: String = AnimeDetailsSection.Related.TYPE

    @Composable
    override fun Render(
        section: AnimeDetailsSection.Related,
        onEvent: (AnimeDetailsUiEvent) -> Unit,
    ) {
        RelatedSection(
            items = section.items,
            onItemClick = { onEvent(AnimeDetailsUiEvent.RelatedAnimeClicked(it)) },
        )
    }
}
