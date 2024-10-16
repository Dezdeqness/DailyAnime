package com.dezdeqness.presentation.features.calendar.composable

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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dezdeqness.R
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.presentation.features.calendar.CalendarUiModel

@Composable
fun CalendarItem(
    modifier: Modifier = Modifier,
    item: CalendarUiModel,
    onClick: (Long) -> Unit,
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .clickable(
                onClick = {
                    onClick(item.id)
                },
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = AppTheme.colors.ripple),
            )
            .fillMaxWidth()
            .then(modifier),
    ) {
        Text(
            text = item.time,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = AppTheme.colors.textPrimary,
            modifier = Modifier
                .weight(1f)
                .align(Alignment.Top)
        )
        Box(
            modifier = Modifier
                .weight(2f)
                .height(150.dp),
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(item.logoUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.ic_placeholder),
                error = painterResource(id = R.drawable.ic_placeholder),
                modifier = Modifier
                    .height(150.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Text(
                text = item.score,
                color = colorResource(id = R.color.search_label),
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(colorResource(id = R.color.background_shadow))
                    .padding(4.dp)
                    .align(alignment = Alignment.BottomEnd)
            )
        }


        Column(
            modifier = Modifier
                .padding(start = 8.dp)
                .weight(3f)
        ) {
            Text(
                text = item.name,
                textAlign = TextAlign.Start,
                fontSize = 16.sp,
                maxLines = 2,
                color = AppTheme.colors.textPrimary,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
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

@Preview(showBackground = true)
@Composable
fun CalendarItemPreview() {
    AppTheme {
        CalendarItem(
            modifier = Modifier.padding(16.dp),
            item = CalendarUiModel(
                id = 1,
                name = "Восстание Лелуша",
                ongoingEpisode = 12,
                type = "tv",
                score = 8.69.toString(),
                time = "12:55",
                logoUrl = "",
            ),
            onClick = {},
        )
    }
}
