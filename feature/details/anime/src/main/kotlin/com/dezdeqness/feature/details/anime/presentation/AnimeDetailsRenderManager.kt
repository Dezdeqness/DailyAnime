package com.dezdeqness.feature.details.anime.presentation

import com.dezdeqness.feature.details.anime.presentation.renderers.CharactersSectionRenderer
import com.dezdeqness.feature.details.anime.presentation.renderers.GenresSectionRenderer
import com.dezdeqness.feature.details.anime.presentation.renderers.MoreInfoSectionRenderer
import com.dezdeqness.feature.details.anime.presentation.renderers.RelatedSectionRenderer
import com.dezdeqness.feature.details.anime.presentation.renderers.ScreenshotsSectionRenderer
import com.dezdeqness.feature.details.anime.presentation.renderers.VideosSectionRenderer
import com.dezdeqness.feature.details.common.presentation.DetailsRenderManager
import com.dezdeqness.feature.details.common.presentation.renderers.BottomSpacerSectionRenderer
import com.dezdeqness.feature.details.common.presentation.renderers.BriefInfoSectionRenderer
import com.dezdeqness.feature.details.common.presentation.renderers.DescriptionSectionRenderer
import com.dezdeqness.feature.details.common.presentation.renderers.HeaderSectionRenderer
import com.dezdeqness.feature.details.common.presentation.renderers.TitleSectionRenderer

fun animeDetailsRenderManager() = DetailsRenderManager(
    renderers = listOf(
        HeaderSectionRenderer,
        TitleSectionRenderer,
        BriefInfoSectionRenderer,
        GenresSectionRenderer,
        DescriptionSectionRenderer,
        MoreInfoSectionRenderer,
        RelatedSectionRenderer,
        CharactersSectionRenderer,
        ScreenshotsSectionRenderer,
        VideosSectionRenderer,
        BottomSpacerSectionRenderer,
    ),
)
