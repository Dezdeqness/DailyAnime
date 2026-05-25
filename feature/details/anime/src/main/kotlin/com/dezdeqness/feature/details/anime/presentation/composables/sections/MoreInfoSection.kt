package com.dezdeqness.feature.details.anime.presentation.composables.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.dezdeqness.feature.details.anime.R
import com.dezdeqness.foundation.ui.theme.AppTheme
import com.dezdeqness.foundation.ui.views.chips.AppChip

@Composable
fun MoreInfoSection(
    modifier: Modifier = Modifier,
    onSimilarClick: () -> Unit,
    onChronologyClick: () -> Unit,
    onStatsClick: () -> Unit,
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
    ) {
        item {
            AppChip(
                title = stringResource(R.string.anime_details_action_similar),
                onClick = onSimilarClick,
            )
        }
        item {
            AppChip(
                title = stringResource(R.string.anime_details_action_chronology),
                onClick = onChronologyClick,
            )
        }
        item {
            AppChip(
                title = stringResource(R.string.anime_details_action_stats),
                onClick = onStatsClick,
            )
        }
    }
}

@PreviewLightDark
@Composable
fun MoreInfoSectionPreview() {
    AppTheme {
        Surface(color = AppTheme.colors.background) {
            MoreInfoSection(
                onSimilarClick = {},
                onChronologyClick = {},
                onStatsClick = {},
            )
        }
    }
}
