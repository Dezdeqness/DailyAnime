package com.dezdeqness.feature.details.anime.presentation.composables.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.dezdeqness.feature.details.anime.presentation.models.GenreChip
import com.dezdeqness.feature.details.anime.presentation.preview.AnimeDetailsPreviewData
import com.dezdeqness.foundation.ui.theme.AppTheme
import com.dezdeqness.foundation.ui.views.chips.AppChip

@Composable
fun GenresSection(
    modifier: Modifier = Modifier,
    items: List<GenreChip>,
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
    ) {
        items(items.size) { index ->
            AppChip(title = items[index].name)
        }
    }
}

@PreviewLightDark
@Composable
fun GenresSectionPreview() {
    AppTheme {
        Surface(color = AppTheme.colors.background) {
            GenresSection(items = AnimeDetailsPreviewData.genres)
        }
    }
}
