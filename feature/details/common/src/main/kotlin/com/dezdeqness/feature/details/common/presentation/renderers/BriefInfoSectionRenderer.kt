package com.dezdeqness.feature.details.common.presentation.renderers

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dezdeqness.feature.details.common.presentation.DetailsSectionRenderer
import com.dezdeqness.feature.details.common.presentation.sections.BriefInfoSection
import com.dezdeqness.foundation.ui.views.details.BriefInfoBlock

object BriefInfoSectionRenderer : DetailsSectionRenderer<BriefInfoSection, Nothing> {
    override val rendererType: String = BriefInfoSection.TYPE

    @Composable
    override fun Render(section: BriefInfoSection, onEvent: (Nothing) -> Unit) {
        BriefInfoBlock(items = section.items, modifier = Modifier.padding(vertical = 8.dp))
    }
}
