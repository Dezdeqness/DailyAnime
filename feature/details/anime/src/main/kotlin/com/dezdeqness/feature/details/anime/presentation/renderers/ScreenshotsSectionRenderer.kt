package com.dezdeqness.feature.details.anime.presentation.renderers

import androidx.compose.runtime.Composable
import com.dezdeqness.feature.details.anime.presentation.AnimeDetailsUiEvent
import com.dezdeqness.feature.details.anime.presentation.composables.sections.ScreenshotsSection
import com.dezdeqness.feature.details.anime.presentation.models.AnimeDetailsSection
import com.dezdeqness.feature.details.common.presentation.DetailsSectionRenderer

object ScreenshotsSectionRenderer :
    DetailsSectionRenderer<AnimeDetailsSection.Screenshots, AnimeDetailsUiEvent> {
    override val rendererType: String = AnimeDetailsSection.Screenshots.TYPE

    @Composable
    override fun Render(
        section: AnimeDetailsSection.Screenshots,
        onEvent: (AnimeDetailsUiEvent) -> Unit,
    ) {
        ScreenshotsSection(
            items = section.items,
            onItemClick = { onEvent(AnimeDetailsUiEvent.ScreenshotClicked(it)) },
        )
    }
}
