package com.dezdeqness.feature.profile

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.dezdeqness.feature.profile.presentation.ProfilePagePreview
import com.dezdeqness.feature.profile.presentation.ProfilePageUnauthorizedPreview

@PreviewLightDark
@Composable
fun ProfilePageTest() {
    ProfilePagePreview()
}

@PreviewLightDark
@Composable
fun ProfilePageUnauthorizedTest() {
    ProfilePageUnauthorizedPreview()
}
