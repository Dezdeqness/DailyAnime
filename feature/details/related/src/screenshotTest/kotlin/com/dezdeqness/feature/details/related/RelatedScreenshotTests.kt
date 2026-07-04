package com.dezdeqness.feature.details.related

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.dezdeqness.feature.details.related.presentation.RelatedListPageEmptyPreview
import com.dezdeqness.feature.details.related.presentation.RelatedListPagePreview
import com.dezdeqness.feature.details.related.presentation.composables.RelatedItemPreview

@PreviewLightDark
@Composable
fun RelatedItemTest() {
    RelatedItemPreview()
}

@PreviewLightDark
@Composable
fun RelatedListPageTest() {
    RelatedListPagePreview()
}

@PreviewLightDark
@Composable
fun RelatedListPageEmptyTest() {
    RelatedListPageEmptyPreview()
}
