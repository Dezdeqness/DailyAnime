package com.dezdeqness.feature.details.anime.presentation.composables.sections

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.dezdeqness.feature.details.anime.R
import com.dezdeqness.feature.details.anime.presentation.models.VideoItem
import com.dezdeqness.feature.details.anime.presentation.preview.AnimeDetailsPreviewData
import com.dezdeqness.foundation.ui.theme.AppTheme
import com.dezdeqness.foundation.ui.views.header.Header
import com.dezdeqness.foundation.ui.views.image.AppImage

@Composable
fun VideosSection(
    modifier: Modifier = Modifier,
    items: List<VideoItem>,
    onItemClick: (String) -> Unit,
) {
    Column(modifier = modifier) {
        Header(
            title = stringResource(R.string.anime_details_section_videos),
            titleStyle = AppTheme.typography.headlineSmall,
            verticalPadding = 8.dp,
        )
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
        ) {
            items(items.size) { index ->
                VideoCard(item = items[index], onClick = onItemClick)
            }
        }
    }
}

@Composable
private fun VideoCard(
    item: VideoItem,
    onClick: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .width(120.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable(
                onClick = { onClick(item.sourceUrl) },
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = AppTheme.colors.ripple),
            )
    ) {
        AppImage(
            data = item.imageUrl,
            modifier = Modifier
                .width(120.dp)
                .height(80.dp),
        )
        Text(
            item.name,
            color = AppTheme.colors.textPrimary,
            style = AppTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
        )
    }
}

@PreviewLightDark
@Composable
fun VideosSectionPreview() {
    AppTheme {
        Surface(color = AppTheme.colors.background) {
            VideosSection(
                items = AnimeDetailsPreviewData.videos,
                onItemClick = {},
            )
        }
    }
}
