package com.dezdeqness.feature.calendar.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dezdeqness.feature.calendar.R
import com.dezdeqness.feature.calendar.presentation.models.CalendarUiModel
import com.dezdeqness.feature.calendar.presentation.preview.CalendarPreviewData
import com.dezdeqness.foundation.ui.theme.AppTheme
import com.dezdeqness.foundation.ui.views.image.AppImage

private const val SCORE_SHADOW_ALPHA = 0.67f

@Composable
fun CalendarItem(
    modifier: Modifier = Modifier,
    item: CalendarUiModel,
    onClick: (Long, String) -> Unit,
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .clickable(
                onClick = {
                    onClick(item.id, item.name)
                },
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = AppTheme.colors.ripple),
            )
            .fillMaxWidth()
            .then(modifier),
    ) {
        Text(
            text = item.time,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = AppTheme.colors.textPrimary,
            modifier = Modifier
                .weight(1f)
                .align(Alignment.Top),
        )
        Box(
            modifier = Modifier
                .weight(2f)
                .height(150.dp),
        ) {
            AppImage(
                data = item.logoUrl,
                modifier = Modifier.height(150.dp),
            )

            Text(
                text = item.score,
                color = AppTheme.colors.white,
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(AppTheme.colors.black.copy(alpha = SCORE_SHADOW_ALPHA))
                    .padding(4.dp)
                    .align(alignment = Alignment.BottomEnd),
            )
        }

        Column(
            modifier = Modifier
                .padding(start = 8.dp)
                .weight(3f),
        ) {
            Text(
                text = item.name,
                textAlign = TextAlign.Start,
                fontSize = 16.sp,
                maxLines = 2,
                color = AppTheme.colors.textPrimary,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth(),
            )
            Text(
                text = stringResource(
                    id = R.string.cell_episode,
                    item.ongoingEpisode,
                ),
                textAlign = TextAlign.Start,
                fontSize = 14.sp,
                color = AppTheme.colors.textPrimary.copy(alpha = 0.8f),
            )
            Text(
                text = item.type,
                textAlign = TextAlign.Start,
                fontSize = 14.sp,
                color = AppTheme.colors.textPrimary.copy(alpha = 0.8f),
            )
        }
    }
}

@PreviewLightDark
@Composable
fun CalendarItemPreview() {
    AppTheme {
        CalendarItem(
            modifier = Modifier.padding(16.dp),
            item = CalendarPreviewData.item,
            onClick = { _, _ -> },
        )
    }
}
