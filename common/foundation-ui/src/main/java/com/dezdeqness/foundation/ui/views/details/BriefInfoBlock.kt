package com.dezdeqness.foundation.ui.views.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dezdeqness.foundation.ui.theme.AppTheme

data class BriefInfoEntry(
    val title: String,
    val info: String,
)

@Composable
fun BriefInfoBlock(
    modifier: Modifier = Modifier,
    items: List<BriefInfoEntry>,
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
    ) {
        items(items.size) { index ->
            BriefInfoItem(item = items[index])
        }
    }
}

@Composable
private fun BriefInfoItem(
    modifier: Modifier = Modifier,
    item: BriefInfoEntry,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(AppTheme.colors.surfaceVariant)
            .widthIn(min = 72.dp)
            .padding(horizontal = 12.dp, vertical = 10.dp),
    ) {
        Text(
            text = item.info,
            style = AppTheme.typography.titleMedium.copy(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
            ),
            color = AppTheme.colors.textPrimary,
        )

        Text(
            text = item.title,
            style = AppTheme.typography.labelMedium,
            color = AppTheme.colors.textSecondary,
            modifier = Modifier.padding(top = 2.dp),
        )
    }
}
