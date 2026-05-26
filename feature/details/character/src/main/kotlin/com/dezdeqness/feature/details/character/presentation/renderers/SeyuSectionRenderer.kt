package com.dezdeqness.feature.details.character.presentation.renderers

import androidx.compose.runtime.Composable
import com.dezdeqness.feature.details.character.presentation.CharacterDetailsUiEvent
import com.dezdeqness.feature.details.character.presentation.composables.sections.SeyuSection
import com.dezdeqness.feature.details.character.presentation.models.CharacterDetailsSection
import com.dezdeqness.feature.details.common.presentation.DetailsSectionRenderer

object SeyuSectionRenderer :
    DetailsSectionRenderer<CharacterDetailsSection.Seyu, CharacterDetailsUiEvent> {
    override val rendererType: String = CharacterDetailsSection.Seyu.TYPE

    @Composable
    override fun Render(
        section: CharacterDetailsSection.Seyu,
        onEvent: (CharacterDetailsUiEvent) -> Unit,
    ) {
        SeyuSection(
            items = section.items,
            onItemClick = { onEvent(CharacterDetailsUiEvent.SeyuClicked(it)) },
        )
    }
}
