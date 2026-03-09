package com.dezdeqness.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.dezdeqness.foundation.ui.views.buttons.AppButtonPreview
import com.dezdeqness.foundation.ui.views.buttons.AppOutlinedButtonPreview
import com.dezdeqness.foundation.ui.views.buttons.AppTextButtonPreview

@PreviewLightDark
@Composable
fun AppButtonTest() {
    AppButtonPreview()
}

@PreviewLightDark
@Composable
fun AppOutlinedTest() {
    AppOutlinedButtonPreview()
}

@PreviewLightDark
@Composable
fun AppTextButtonTest() {
    AppTextButtonPreview()
}
