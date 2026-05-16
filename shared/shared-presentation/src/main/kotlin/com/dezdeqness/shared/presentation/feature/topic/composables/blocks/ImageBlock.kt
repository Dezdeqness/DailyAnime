package com.dezdeqness.shared.presentation.feature.topic.composables.blocks

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.dezdeqness.foundation.ui.views.image.AppImage

@Composable
fun ImageBlock(
    url: String,
    modifier: Modifier = Modifier,
) {
    AppImage(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .padding(vertical = 4.dp),
        data = url,
        contentScale = ContentScale.FillWidth,
    )
}
