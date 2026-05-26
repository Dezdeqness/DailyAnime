package com.dezdeqness.feature.details.common.presentation.utils

import androidx.compose.runtime.staticCompositionLocalOf
import com.dezdeqness.feature.details.common.presentation.DetailsRenderManager

val LocalDetailsRenderManagerComposition = staticCompositionLocalOf<DetailsRenderManager<*>> {
    error("No LocalDetailsRenderManagerComposition provided. Wrap with CompositionLocalProvider.")
}
