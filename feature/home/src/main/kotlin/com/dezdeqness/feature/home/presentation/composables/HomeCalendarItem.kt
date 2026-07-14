package com.dezdeqness.feature.home.presentation.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dezdeqness.feature.home.presentation.models.HomeCalendarUiModel
import com.dezdeqness.foundation.ui.theme.AppTheme
import com.dezdeqness.foundation.ui.views.image.AppImage

@Composable
fun HomeCalendarItem(
    modifier: Modifier = Modifier,
    item: HomeCalendarUiModel,
    onClick: (Long, String) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .clickable(
                onClick = {
                    onClick(item.id, item.title)
                },
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = AppTheme.colors.ripple),
            ),
    ) {
        AppImage(
            data = item.imageUrl,
            modifier = Modifier
                .height(160.dp)
                .aspectRatio(2 / 3f),
        )

        Column(modifier = Modifier.padding(horizontal = 8.dp)) {
            Text(
                text = item.title,
                textAlign = TextAlign.Start,
                fontSize = 16.sp,
                maxLines = 2,
                style = AppTheme.typography.bodyMedium,
                color = AppTheme.colors.textPrimary,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth(),
            )
            Text(
                text = item.description,
                textAlign = TextAlign.Start,
                style = AppTheme.typography.bodySmall,
                fontSize = 14.sp,
                maxLines = 5,
                overflow = TextOverflow.Ellipsis,
                color = AppTheme.colors.textPrimary.copy(alpha = 0.8f),
            )
        }
    }
}
