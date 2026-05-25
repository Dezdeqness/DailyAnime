package com.dezdeqness.feature.details.anime.presentation.composables.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dezdeqness.feature.details.anime.R
import com.dezdeqness.foundation.ui.theme.AppTheme
import com.dezdeqness.foundation.ui.views.text.HtmlDescription

@Composable
fun DescriptionSection(
    modifier: Modifier = Modifier,
    html: String,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = stringResource(R.string.anime_details_section_description),
            style = AppTheme.typography.headlineSmall,
            color = AppTheme.colors.textPrimary,
        )
        HtmlDescription(html = html)
    }
}
