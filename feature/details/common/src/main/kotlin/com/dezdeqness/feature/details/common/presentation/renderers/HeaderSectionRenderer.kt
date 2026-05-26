package com.dezdeqness.feature.details.common.presentation.renderers

import androidx.compose.runtime.Composable
import com.dezdeqness.feature.details.common.presentation.DetailsSectionRenderer
import com.dezdeqness.feature.details.common.presentation.sections.HeaderSection
import com.dezdeqness.foundation.ui.views.details.PosterHeader

object HeaderSectionRenderer : DetailsSectionRenderer<HeaderSection, Nothing> {
    override val rendererType: String = HeaderSection.TYPE

    @Composable
    override fun Render(section: HeaderSection, onEvent: (Nothing) -> Unit) {
        PosterHeader(imageUrl = section.imageUrl, rating = section.rating)
    }
}
