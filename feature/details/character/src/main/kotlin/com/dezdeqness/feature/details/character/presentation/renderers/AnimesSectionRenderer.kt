package com.dezdeqness.feature.details.character.presentation.renderers

import androidx.compose.runtime.Composable
import com.dezdeqness.feature.details.character.presentation.CharacterDetailsUiEvent
import com.dezdeqness.feature.details.character.presentation.composables.sections.AnimesSection
import com.dezdeqness.feature.details.character.presentation.models.CharacterDetailsSection
import com.dezdeqness.feature.details.common.presentation.DetailsSectionRenderer

object AnimesSectionRenderer :
    DetailsSectionRenderer<CharacterDetailsSection.Animes, CharacterDetailsUiEvent> {
    override val rendererType: String = CharacterDetailsSection.Animes.TYPE

    @Composable
    override fun Render(
        section: CharacterDetailsSection.Animes,
        onEvent: (CharacterDetailsUiEvent) -> Unit,
    ) {
        AnimesSection(
            items = section.items,
            onItemClick = { onEvent(CharacterDetailsUiEvent.AnimeClicked(it)) },
        )
    }
}
