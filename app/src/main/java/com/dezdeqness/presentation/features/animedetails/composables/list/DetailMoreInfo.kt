package com.dezdeqness.presentation.features.animedetails.composables.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dezdeqness.R
import com.dezdeqness.core.ui.views.chips.AppChip
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
            AppChip(
                title = stringResource(R.string.anime_toolbar_similar),
                onClick = {
                    onAction(Action.SimilarClicked)
                }
            )
        }

        item {
            AppChip(
                title = stringResource(R.string.anime_toolbar_chronology),
                onClick = {
                    onAction(Action.ChronologyClicked)
                }
            )
        }
        item {
            AppChip(
                title = stringResource( R.string.anime_toolbar_stats),
                onClick = {
                    onAction(Action.StatsClicked)
                }
            )
        }
    }
}
