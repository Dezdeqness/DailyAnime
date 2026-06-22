package com.dezdeqness.feature.onboarding.flow.presentation.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dezdeqness.feature.onboarding.R
import com.dezdeqness.feature.onboarding.selectgenres.presentation.composables.SectionBlock
import com.dezdeqness.feature.onboarding.selectgenres.presentation.models.GenreUiModel
import com.dezdeqness.foundation.ui.theme.AppTheme

@Composable
fun GenresStep(
    genres: List<GenreUiModel>,
    selectedIds: Set<String>,
    minSelection: Int,
    onGenreClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val genreItems = remember(genres) { genres.filter { it.isGenre } }
    val themeItems = remember(genres) { genres.filter { !it.isGenre } }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
    ) {
        Text(
            text = stringResource(R.string.onboarding_genres_title),
            style = AppTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold),
            color = AppTheme.colors.textPrimary,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.onboarding_genres_subtitle),
            style = AppTheme.typography.bodyMedium,
            color = AppTheme.colors.textSecondary,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.onboarding_genres_counter, selectedIds.size, minSelection),
            style = AppTheme.typography.bodySmall,
            color = AppTheme.colors.primary,
        )
        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
        ) {
            SectionBlock(
                title = stringResource(R.string.onboarding_select_genres_title_genres),
                items = genreItems,
                selectedIds = selectedIds,
                onItemClick = onGenreClick,
            )
            Spacer(modifier = Modifier.height(16.dp))
            SectionBlock(
                title = stringResource(R.string.onboarding_select_genres_title_themes),
                items = themeItems,
                selectedIds = selectedIds,
                onItemClick = onGenreClick,
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
