package com.dezdeqness.presentation.features.home.composable

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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dezdeqness.R
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.presentation.action.Action
import com.dezdeqness.presentation.action.Action.AnimeClick
import com.dezdeqness.presentation.features.home.model.HomeCalendarUiModel

@Composable
fun HomeCalendarItem(
    modifier: Modifier = Modifier,
    item: HomeCalendarUiModel,
    onActionReceive: (Action) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .clickable(
                onClick = {
                    onActionReceive(AnimeClick(item.id))
                },
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = AppTheme.colors.ripple)
            ),
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(item.imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.ic_placeholder),
            error = painterResource(id = R.drawable.ic_placeholder),
            modifier = Modifier
                .height(160.dp)
                .aspectRatio(9f / 16)
                .clip(RoundedCornerShape(8.dp))
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
                modifier = Modifier.fillMaxWidth()
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
