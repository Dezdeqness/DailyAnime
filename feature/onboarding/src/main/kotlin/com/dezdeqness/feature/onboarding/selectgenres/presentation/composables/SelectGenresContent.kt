package com.dezdeqness.feature.onboarding.selectgenres.presentation.composables

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.feature.onboarding.R
import com.dezdeqness.feature.onboarding.selectgenres.presentation.models.GenreUiModel

private const val MAX_SELECTION = 3

@Composable
fun SelectGenresContent(
    list: List<GenreUiModel>,
    selectedIds: Set<String>,
    onGenreClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val genres = remember(list.size) { list.filter { it.isGenre } }
    val themes = remember(list.size) { list.filter { !it.isGenre } }

    Column(modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.onboarding_select_genres_header),
                style = AppTheme.typography.headlineMedium,
                color = AppTheme.colors.textPrimary,
            )

            Box(
                modifier = Modifier
                    .background(
                        color = AppTheme.colors.primary.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "${selectedIds.size} / $MAX_SELECTION",
                    style = AppTheme.typography.bodySmall.copy(
                        color = AppTheme.colors.primary
                    )
                )
            }
        }

        Column(
            modifier = Modifier
                .animateContentSize()
                .verticalScroll(rememberScrollState())
        ) {
            SectionBlock(
                title = stringResource(R.string.onboarding_select_genres_title_genres),
                items = genres,
                selectedIds = selectedIds,
                onItemClick = onGenreClick
            )

            SectionBlock(
                title = stringResource(R.string.onboarding_select_genres_title_themes),
                items = themes,
                selectedIds = selectedIds,
                onItemClick = onGenreClick
            )

            Spacer(modifier = Modifier.size(64.dp))
        }
    }
}
