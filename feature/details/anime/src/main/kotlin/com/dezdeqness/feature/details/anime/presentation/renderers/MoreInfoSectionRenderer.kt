package com.dezdeqness.feature.details.anime.presentation.renderers

import androidx.compose.runtime.Composable
import com.dezdeqness.feature.details.anime.presentation.AnimeDetailsUiEvent
import com.dezdeqness.feature.details.anime.presentation.composables.sections.MoreInfoSection
import com.dezdeqness.feature.details.anime.presentation.models.AnimeDetailsSection
import com.dezdeqness.feature.details.common.presentation.DetailsSectionRenderer

object MoreInfoSectionRenderer :
    DetailsSectionRenderer<AnimeDetailsSection.MoreInfo, AnimeDetailsUiEvent> {
    override val rendererType: String = AnimeDetailsSection.MoreInfo.TYPE

    @Composable
    override fun Render(
        section: AnimeDetailsSection.MoreInfo,
        onEvent: (AnimeDetailsUiEvent) -> Unit,
    ) {
        MoreInfoSection(
            onSimilarClick = { onEvent(AnimeDetailsUiEvent.SimilarClicked) },
            onChronologyClick = { onEvent(AnimeDetailsUiEvent.ChronologyClicked) },
            onStatsClick = { onEvent(AnimeDetailsUiEvent.StatsClicked) },
        )
    }
}
