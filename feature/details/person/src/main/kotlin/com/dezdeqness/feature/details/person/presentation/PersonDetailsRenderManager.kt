package com.dezdeqness.feature.details.person.presentation

import com.dezdeqness.feature.details.common.presentation.DetailsRenderManager
import com.dezdeqness.feature.details.common.presentation.renderers.BottomSpacerSectionRenderer
import com.dezdeqness.feature.details.common.presentation.renderers.BriefInfoSectionRenderer
import com.dezdeqness.feature.details.common.presentation.renderers.HeaderSectionRenderer
import com.dezdeqness.feature.details.common.presentation.renderers.TitleSectionRenderer

fun personDetailsRenderManager() = DetailsRenderManager<PersonDetailsUiEvent>(
    renderers = listOf(
        HeaderSectionRenderer,
        TitleSectionRenderer,
        BriefInfoSectionRenderer,
        BottomSpacerSectionRenderer,
    ),
)
