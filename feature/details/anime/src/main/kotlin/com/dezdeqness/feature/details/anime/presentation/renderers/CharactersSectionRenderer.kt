package com.dezdeqness.feature.details.anime.presentation.renderers

import androidx.compose.runtime.Composable
import com.dezdeqness.feature.details.anime.presentation.AnimeDetailsUiEvent
import com.dezdeqness.feature.details.anime.presentation.composables.sections.CharactersSection
import com.dezdeqness.feature.details.anime.presentation.models.AnimeDetailsSection
import com.dezdeqness.feature.details.common.presentation.DetailsSectionRenderer

object CharactersSectionRenderer :
    DetailsSectionRenderer<AnimeDetailsSection.Characters, AnimeDetailsUiEvent> {
    override val rendererType: String = AnimeDetailsSection.Characters.TYPE

    @Composable
    override fun Render(
        section: AnimeDetailsSection.Characters,
        onEvent: (AnimeDetailsUiEvent) -> Unit,
    ) {
        CharactersSection(
            items = section.items,
            onItemClick = { onEvent(AnimeDetailsUiEvent.CharacterClicked(it)) },
        )
    }
}
