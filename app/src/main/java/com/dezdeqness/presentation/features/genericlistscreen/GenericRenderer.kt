package com.dezdeqness.presentation.features.genericlistscreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import com.dezdeqness.presentation.action.Action
import com.dezdeqness.presentation.models.AdapterItem

interface GenericRenderer {
    @Composable
    fun Render(item: AdapterItem, onClick: (Action) -> Unit)
}

val LocalAdapterItemRenderer = staticCompositionLocalOf<GenericRenderer?> {
    null
}
