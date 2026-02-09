package com.dezdeqness.feature.personallist.search.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.core.ui.views.image.AppImage
import com.dezdeqness.feature.personallist.search.model.SearchUserRateUiModel

@Composable
fun PersonalListSearchItem(
    modifier: Modifier = Modifier,
    model: SearchUserRateUiModel,
    onAnimeClick: (animeId: Long, title: String) -> Unit,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .clickable(
                onClick = {
                    onAnimeClick(
                        model.id,
                        model.name
                    )
                },
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = AppTheme.colors.ripple),
            )
            .fillMaxWidth(),
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Max)) {
            AppImage(
                data = model.logoUrl,
                contentScale = ContentScale.FillHeight,
                modifier = Modifier
                    .height(150.dp)
                    .aspectRatio(2f / 3)
            )

            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = model.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        maxLines = 2,
                        color = AppTheme.colors.textPrimary,
                        style = AppTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = model.kind,
                        textAlign = TextAlign.Start,
                        fontSize = 12.sp,
                        style = AppTheme.typography.bodyMedium,
                        color = AppTheme.colors.textPrimary.copy(alpha = 0.8f),
                        modifier = Modifier.padding(start = 8.dp),
                    )
                }
            }

        }
    }
}
