package com.dezdeqness.feature.details.common.presentation

import androidx.compose.runtime.Composable

class DetailsRenderManager<UiEvent : Any>(
    renderers: List<DetailsSectionRenderer<*, UiEvent>>,
) {
    private val renderers: DetailsSectionRendererMap = renderers.associateBy { it.rendererType }

    @Suppress("UNCHECKED_CAST")
    @Composable
    fun <UiEvent : Any> OnRenderByType(section: DetailsSection, onEvent: (UiEvent) -> Unit) {
        renderers[section.rendererType]?.let {
            (it as DetailsSectionRenderer<DetailsSection, UiEvent>).Render(section, onEvent)
        }
    }
}
