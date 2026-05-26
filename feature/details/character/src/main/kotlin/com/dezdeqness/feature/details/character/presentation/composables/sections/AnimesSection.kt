package com.dezdeqness.feature.details.character.presentation.composables.sections

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.dezdeqness.feature.details.character.R
import com.dezdeqness.feature.details.character.presentation.models.AnimeItem
import com.dezdeqness.feature.details.character.presentation.preview.CharacterDetailsPreviewData
import com.dezdeqness.foundation.ui.theme.AppTheme
import com.dezdeqness.foundation.ui.views.header.Header
import com.dezdeqness.foundation.ui.views.image.AppImage

@Composable
fun AnimesSection(
    modifier: Modifier = Modifier,
    items: List<AnimeItem>,
    onItemClick: (Long) -> Unit,
) {
    Column(modifier = modifier) {
        Header(
            title = stringResource(R.string.character_details_section_animes),
            titleStyle = AppTheme.typography.headlineSmall,
            verticalPadding = 8.dp,
        )
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
        ) {
            items(items.size) { index ->
                AnimeCard(item = items[index], onClick = onItemClick)
            }
        }
    }
}

@Composable
private fun AnimeCard(
    item: AnimeItem,
    onClick: (Long) -> Unit,
) {
    Column(
        modifier = Modifier
            .width(120.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable(
                onClick = { onClick(item.id) },
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = AppTheme.colors.ripple),
            )
    ) {
        AppImage(
            data = item.imageUrl,
            modifier = Modifier
                .width(120.dp)
                .height(170.dp),
        )
        Box(
            modifier = Modifier.padding(bottom = 2.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                item.title,
                color = AppTheme.colors.textPrimary,
                style = AppTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
                modifier = Modifier.fillMaxWidth(),
            )
            Text(
                text = "",
                style = AppTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
            )
        }
        Text(
            item.kind.uppercase(),
            color = AppTheme.colors.textPrimary.copy(alpha = 0.7f),
            style = AppTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@PreviewLightDark
@Composable
fun AnimesSectionPreview() {
    AppTheme {
        Surface(color = AppTheme.colors.background) {
            AnimesSection(
                items = CharacterDetailsPreviewData.animes,
                onItemClick = {},
            )
        }
    }
}
