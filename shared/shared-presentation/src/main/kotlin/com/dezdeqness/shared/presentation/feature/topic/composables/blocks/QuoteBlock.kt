package com.dezdeqness.shared.presentation.feature.topic.composables.blocks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.dezdeqness.foundation.ui.theme.AppTheme
import com.dezdeqness.shared.presentation.feature.topic.model.ParagraphBlock

@Composable
fun QuoteBlock(
    blocks: List<ParagraphBlock>,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .padding(vertical = 4.dp)
            .height(IntrinsicSize.Min),
    ) {
        Box(
            modifier = Modifier
                .width(3.dp)
                .fillMaxHeight()
                .clip(RoundedCornerShape(1.5.dp))
                .background(AppTheme.colors.textSecondary),
        )
        ContentBlockRenderer(
            blocks = blocks,
            modifier = Modifier.padding(start = 12.dp),
        )
    }
}
