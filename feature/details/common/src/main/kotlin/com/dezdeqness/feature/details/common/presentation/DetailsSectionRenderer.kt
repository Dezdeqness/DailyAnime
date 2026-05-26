package com.dezdeqness.feature.details.common.presentation

import androidx.compose.runtime.Composable

interface DetailsSectionRenderer<Section : DetailsSection, out UiEvent : Any> {
    val rendererType: String

    @Composable
    fun Render(section: Section, onEvent: (UiEvent) -> Unit)
}

typealias DetailsSectionRendererMap = Map<String, DetailsSectionRenderer<*, *>>
