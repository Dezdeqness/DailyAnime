package com.dezdeqness.feature.details.anime.presentation.composables.sections

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Surface
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.dezdeqness.feature.details.anime.R
import com.dezdeqness.feature.details.anime.presentation.models.ScreenshotItem
import com.dezdeqness.feature.details.anime.presentation.preview.AnimeDetailsPreviewData
import com.dezdeqness.foundation.ui.theme.AppTheme
import com.dezdeqness.foundation.ui.views.header.Header
import com.dezdeqness.foundation.ui.views.image.AppImage

@Composable
fun ScreenshotsSection(
    modifier: Modifier = Modifier,
    items: List<ScreenshotItem>,
    onItemClick: (String) -> Unit,
) {
    Column(modifier = modifier) {
        Header(
            title = stringResource(R.string.anime_details_section_screenshots),
            titleStyle = AppTheme.typography.headlineSmall,
            verticalPadding = 8.dp,
        )
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
        ) {
            items(items.size) { index ->
                val item = items[index]
                AppImage(
                    data = item.previewUrl,
                    modifier = Modifier
                        .width(120.dp)
                        .height(80.dp)
                        .clickable(
                            onClick = { onItemClick(item.previewUrl) },
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(color = AppTheme.colors.ripple),
                        ),
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
fun ScreenshotsSectionPreview() {
    AppTheme {
        Surface(color = AppTheme.colors.background) {
            ScreenshotsSection(
                items = AnimeDetailsPreviewData.screenshots,
                onItemClick = {},
            )
        }
    }
}
