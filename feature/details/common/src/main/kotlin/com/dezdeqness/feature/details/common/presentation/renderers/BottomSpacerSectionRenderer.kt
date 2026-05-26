package com.dezdeqness.feature.details.common.presentation.renderers

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dezdeqness.feature.details.common.presentation.DetailsSectionRenderer
import com.dezdeqness.feature.details.common.presentation.sections.BottomSpacerSection

object BottomSpacerSectionRenderer : DetailsSectionRenderer<BottomSpacerSection, Nothing> {
    override val rendererType: String = BottomSpacerSection.TYPE

    @Composable
    override fun Render(section: BottomSpacerSection, onEvent: (Nothing) -> Unit) {
        Spacer(modifier = Modifier.height(160.dp))
    }
}
