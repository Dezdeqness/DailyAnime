package com.dezdeqness.feature.details.anime.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.dezdeqness.feature.details.anime.presentation.AnimeDetailsActions
import com.dezdeqness.feature.details.anime.presentation.composables.sections.CharactersSection
import com.dezdeqness.feature.details.anime.presentation.composables.sections.DescriptionSection
import com.dezdeqness.feature.details.anime.presentation.composables.sections.GenresSection
import com.dezdeqness.feature.details.anime.presentation.composables.sections.MoreInfoSection
import com.dezdeqness.feature.details.anime.presentation.composables.sections.RelatedSection
import com.dezdeqness.feature.details.anime.presentation.composables.sections.ScreenshotsSection
import com.dezdeqness.feature.details.anime.presentation.composables.sections.TitleSection
import com.dezdeqness.feature.details.anime.presentation.composables.sections.VideosSection
import com.dezdeqness.feature.details.anime.presentation.models.AnimeDetailsSection
import com.dezdeqness.feature.details.anime.presentation.preview.AnimeDetailsPreviewData
import com.dezdeqness.feature.userrate.EditRateUiModel
import com.dezdeqness.foundation.ui.theme.AppTheme
import com.dezdeqness.foundation.ui.views.details.BriefInfoBlock
import com.dezdeqness.foundation.ui.views.details.PosterHeader

@Composable
fun AnimeDetailsContent(
    modifier: Modifier = Modifier,
    sections: List<AnimeDetailsSection>,
    actions: AnimeDetailsActions,
    state: LazyListState = rememberLazyListState(),
) {
    LazyColumn(
        state = state,
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.colors.onPrimary),
    ) {
        items(
            count = sections.size,
            key = { index -> sections[index]::class.simpleName + index },
        ) { index ->
            when (val section = sections[index]) {
                is AnimeDetailsSection.Header -> PosterHeader(
                    imageUrl = section.imageUrl,
                    rating = section.rating,
                )

                is AnimeDetailsSection.Title -> TitleSection(
                    title = section.text,
                    modifier = Modifier.fillMaxWidth(),
                )

                is AnimeDetailsSection.BriefInfo -> BriefInfoBlock(
                    items = section.items,
                    modifier = Modifier.padding(vertical = 8.dp),
                )

                is AnimeDetailsSection.Genres -> GenresSection(items = section.items)

                is AnimeDetailsSection.Description -> DescriptionSection(
                    html = section.html,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )

                AnimeDetailsSection.MoreInfo -> MoreInfoSection(
                    onSimilarClick = actions::onSimilarClicked,
                    onChronologyClick = actions::onChronologyClicked,
                    onStatsClick = actions::onStatsClicked,
                )

                is AnimeDetailsSection.Related -> RelatedSection(
                    items = section.items,
                    onItemClick = actions::onRelatedAnimeClicked,
                )

                is AnimeDetailsSection.Characters -> CharactersSection(
                    items = section.items,
                    onItemClick = actions::onCharacterClicked,
                )

                is AnimeDetailsSection.Screenshots -> ScreenshotsSection(
                    items = section.items,
                    onItemClick = actions::onScreenshotClicked,
                )

                is AnimeDetailsSection.Videos -> VideosSection(
                    items = section.items,
                    onItemClick = actions::onVideoClicked,
                )

                AnimeDetailsSection.BottomSpacer -> Spacer(modifier = Modifier.height(160.dp))
            }
        }
    }
}

private object PreviewAnimeDetailsActions : AnimeDetailsActions {
    override fun onBackPressed() = Unit
    override fun onSharePressed() = Unit
    override fun onRetryClicked() = Unit
    override fun onEditRateClicked() = Unit
    override fun onUserRateChanged(userRate: EditRateUiModel) = Unit
    override fun onUserRateBottomDialogClosed() = Unit
    override fun onStatsClicked() = Unit
    override fun onSimilarClicked() = Unit
    override fun onChronologyClicked() = Unit
    override fun onRelatedAnimeClicked(animeId: Long) = Unit
    override fun onCharacterClicked(characterId: Long) = Unit
    override fun onScreenshotClicked(previewUrl: String) = Unit
    override fun onVideoClicked(url: String) = Unit
}

@PreviewLightDark
@Composable
fun AnimeDetailsContentPreview() {
    AppTheme {
        Surface(color = AppTheme.colors.background) {
            AnimeDetailsContent(
                sections = AnimeDetailsPreviewData.sections,
                actions = PreviewAnimeDetailsActions,
            )
        }
    }
}
