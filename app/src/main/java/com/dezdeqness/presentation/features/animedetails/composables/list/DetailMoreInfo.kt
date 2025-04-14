package com.dezdeqness.presentation.features.animedetails.composables.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dezdeqness.R
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.presentation.action.Action

@Composable
fun DetailMoreInfo(
    modifier: Modifier = Modifier,
    onAction: (Action) -> Unit,
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        item {
            MoreInfoItem(
                textRes = R.string.anime_toolbar_similar,
                onClick = {
                    onAction(Action.SimilarClicked)
                }
            )
        }

        item {
            MoreInfoItem(
                textRes = R.string.anime_toolbar_chronology,
                onClick = {
                    onAction(Action.ChronologyClicked)
                }
            )
        }
        item {
            MoreInfoItem(
                textRes = R.string.anime_toolbar_stats,
                onClick = {
                    onAction(Action.StatsClicked)
                }
            )
        }
    }
}

@Composable
fun MoreInfoItem(
    modifier: Modifier = Modifier,
    textRes: Int,
    onClick: () -> Unit
) {
    FilterChip(
        modifier = modifier,
        onClick = onClick,
        label = {
            Text(
                text = stringResource(id = textRes),
                color = AppTheme.colors.textPrimary,
                style = AppTheme.typography.bodyMedium,
            )
        },
        colors = FilterChipDefaults.filterChipColors().copy(
            containerColor = colorResource(R.color.background_tint),
            selectedContainerColor = colorResource(R.color.background_tint)
        ),
        shape = RoundedCornerShape(50),
        elevation = null,
        selected = false,
    )
}
