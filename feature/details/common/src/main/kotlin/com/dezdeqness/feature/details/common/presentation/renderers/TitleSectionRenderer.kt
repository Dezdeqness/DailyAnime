package com.dezdeqness.feature.details.common.presentation.renderers

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dezdeqness.feature.details.common.presentation.DetailsSectionRenderer
import com.dezdeqness.feature.details.common.presentation.sections.TitleSection
import com.dezdeqness.foundation.ui.theme.AppTheme

object TitleSectionRenderer : DetailsSectionRenderer<TitleSection, Nothing> {
    override val rendererType: String = TitleSection.TYPE

    @Composable
    override fun Render(section: TitleSection, onEvent: (Nothing) -> Unit) {
        Text(
            text = section.text,
            color = AppTheme.colors.textPrimary,
            fontSize = 18.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp),
            textAlign = TextAlign.Center,
        )
    }
}
