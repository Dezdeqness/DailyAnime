package com.dezdeqness.feature.calendar

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.dezdeqness.feature.calendar.presentation.CalendarPageLoadingPreview
import com.dezdeqness.feature.calendar.presentation.CalendarPagePreview
import com.dezdeqness.feature.calendar.presentation.composables.CalendarItemPreview

@PreviewLightDark
@Composable
fun CalendarItemTest() {
    CalendarItemPreview()
}

@PreviewLightDark
@Composable
fun CalendarPageTest() {
    CalendarPagePreview()
}

@PreviewLightDark
@Composable
fun CalendarPageLoadingTest() {
    CalendarPageLoadingPreview()
}
