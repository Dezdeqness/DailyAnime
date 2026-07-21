package com.dezdeqness.feature.search.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.dezdeqness.feature.search.presentation.models.AnimeUiModel
import com.dezdeqness.foundation.ui.theme.AppTheme
import com.dezdeqness.foundation.ui.views.image.AppImage

private const val KIND_SHADOW_ALPHA = 0.67f

@Composable
fun AnimeItem(
    modifier: Modifier = Modifier,
    item: AnimeUiModel,
    onClick: (Long) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .clickable(
                onClick = {
                    onClick(item.id)
                },
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = AppTheme.colors.ripple),
            ),
    ) {
        Box {
            AppImage(
                data = item.logoUrl,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .align(alignment = Alignment.Center),
            )

            Text(
                text = item.kind,
                color = AppTheme.colors.white,
                style = AppTheme.typography.bodySmall,
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(AppTheme.colors.black.copy(alpha = KIND_SHADOW_ALPHA))
                    .padding(4.dp)
                    .align(alignment = Alignment.TopStart),
            )
        }

        Box(contentAlignment = Alignment.Center) {
            Text(
                text = item.title,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                maxLines = 2,
                style = AppTheme.typography.bodySmall,
                color = AppTheme.colors.textPrimary,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
            )

            Text(
                text = "",
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
                style = AppTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@PreviewLightDark
@Composable
fun AnimeItemPreview() {
    AppTheme {
        Box(
            modifier = Modifier
                .background(AppTheme.colors.onPrimary)
                .width(200.dp),
        ) {
            AnimeItem(
                item = AnimeUiModel(
                    id = 321,
                    logoUrl = "url",
                    title = "Code Geass\nRebellion Lelouch",
                    kind = "TV",
                ),
                onClick = {},
            )
        }
    }
}
