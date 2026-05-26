package com.dezdeqness.feature.details.character.presentation

import com.dezdeqness.feature.details.character.presentation.renderers.AnimesSectionRenderer
import com.dezdeqness.feature.details.character.presentation.renderers.SeyuSectionRenderer
import com.dezdeqness.feature.details.common.presentation.DetailsRenderManager
import com.dezdeqness.feature.details.common.presentation.renderers.BottomSpacerSectionRenderer
import com.dezdeqness.feature.details.common.presentation.renderers.DescriptionSectionRenderer
import com.dezdeqness.feature.details.common.presentation.renderers.HeaderSectionRenderer
import com.dezdeqness.feature.details.common.presentation.renderers.TitleSectionRenderer

fun characterDetailsRenderManager() = DetailsRenderManager(
    renderers = listOf(
        HeaderSectionRenderer,
        TitleSectionRenderer,
        DescriptionSectionRenderer,
        SeyuSectionRenderer,
        AnimesSectionRenderer,
        BottomSpacerSectionRenderer,
    ),
)
