package com.dezdeqness.feature.details.common.presentation.renderers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dezdeqness.feature.details.common.R
import com.dezdeqness.feature.details.common.presentation.DetailsSectionRenderer
import com.dezdeqness.feature.details.common.presentation.sections.DescriptionSection
import com.dezdeqness.foundation.ui.theme.AppTheme
import com.dezdeqness.foundation.ui.views.text.HtmlDescription

object DescriptionSectionRenderer : DetailsSectionRenderer<DescriptionSection, Nothing> {
    override val rendererType: String = DescriptionSection.TYPE

    @Composable
    override fun Render(section: DescriptionSection, onEvent: (Nothing) -> Unit) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = stringResource(R.string.details_section_description),
                style = AppTheme.typography.headlineSmall,
                color = AppTheme.colors.textPrimary,
            )
            HtmlDescription(html = section.html)
        }
    }
}
