package com.dezdeqness.feature.details.anime

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.dezdeqness.feature.details.anime.presentation.AnimeDetailsContentPreview
import com.dezdeqness.feature.details.anime.presentation.composables.sections.CharactersSectionPreview
import com.dezdeqness.feature.details.anime.presentation.composables.sections.GenresSectionPreview
import com.dezdeqness.feature.details.anime.presentation.composables.sections.MoreInfoSectionPreview
import com.dezdeqness.feature.details.anime.presentation.composables.sections.RelatedSectionPreview
import com.dezdeqness.feature.details.anime.presentation.composables.sections.ScreenshotsSectionPreview
import com.dezdeqness.feature.details.anime.presentation.composables.sections.VideosSectionPreview

@PreviewLightDark
@Composable
fun GenresSectionTest() {
    GenresSectionPreview()
}

@PreviewLightDark
@Composable
fun MoreInfoSectionTest() {
    MoreInfoSectionPreview()
}

@PreviewLightDark
@Composable
fun RelatedSectionTest() {
    RelatedSectionPreview()
}

@PreviewLightDark
@Composable
fun CharactersSectionTest() {
    CharactersSectionPreview()
}

@PreviewLightDark
@Composable
fun ScreenshotsSectionTest() {
    ScreenshotsSectionPreview()
}

@PreviewLightDark
@Composable
fun VideosSectionTest() {
    VideosSectionPreview()
}

@PreviewLightDark
@Composable
fun AnimeDetailsContentTest() {
    AnimeDetailsContentPreview()
}
