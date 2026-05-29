package com.dezdeqness.feature.history

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.dezdeqness.feature.history.presentation.composables.HistoryHeaderPreview
import com.dezdeqness.feature.history.presentation.composables.HistoryItemPreview
import com.dezdeqness.feature.history.presentation.composables.HistoryListPreview

@PreviewLightDark
@Composable
fun HistoryItemTest() {
    HistoryItemPreview()
}

@PreviewLightDark
@Composable
fun HistoryHeaderTest() {
    HistoryHeaderPreview()
}

@PreviewLightDark
@Composable
fun HistoryListTest() {
    HistoryListPreview()
}
