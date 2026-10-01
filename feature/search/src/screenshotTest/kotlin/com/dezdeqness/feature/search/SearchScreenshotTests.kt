package com.dezdeqness.feature.search

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.dezdeqness.feature.search.presentation.AnimeSearchPagePreview
import com.dezdeqness.feature.search.presentation.composables.AnimeItemPreview
import com.dezdeqness.feature.search.presentation.history.SearchHistoryPreview

@PreviewLightDark
@Composable
fun AnimeItemTest() {
    AnimeItemPreview()
}

@PreviewLightDark
@Composable
fun AnimeSearchPageTest() {
    AnimeSearchPagePreview()
}

@PreviewLightDark
@Composable
fun SearchHistoryTest() {
    SearchHistoryPreview()
}
