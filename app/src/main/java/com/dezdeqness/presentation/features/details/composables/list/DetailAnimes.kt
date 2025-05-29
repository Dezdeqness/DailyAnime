package com.dezdeqness.presentation.features.details.composables.list

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
import androidx.compose.ui.unit.dp
import com.dezdeqness.R
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.core.ui.views.header.Header
import com.dezdeqness.core.ui.views.image.AppImage
import com.dezdeqness.presentation.features.animelist.AnimeUiModel
import com.google.common.collect.ImmutableList

@Composable
fun DetailAnimes(
    modifier: Modifier = Modifier,
    animeList: ImmutableList<AnimeUiModel>,
    onAnimeClick: (Long) -> Unit
) {
    Column(modifier = modifier) {
        Header(
            title = stringResource(R.string.anime_details_anime_title),
            titleStyle = AppTheme.typography.headlineSmall,
            verticalPadding = 8.dp,
        )

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(animeList.size) { index ->
                val related = animeList[index]

                AnimeItem(
                    related = related,
                    onAnimeClick = onAnimeClick,
                )
            }
        }
    }
}

@Composable
private fun AnimeItem(
    modifier: Modifier = Modifier,
    related: AnimeUiModel,
    onAnimeClick: (Long) -> Unit,
) {
    Column(
        modifier = modifier
            .width(120.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable(
                onClick = { onAnimeClick(related.id) },
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = AppTheme.colors.ripple),
            )
    ) {
        AppImage(
            data = related.logoUrl,
            modifier = Modifier
                .width(120.dp)
                .height(170.dp),
        )

        Box(
            modifier = Modifier.padding(bottom = 2.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                related.title,
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
            related.kind,
            color = AppTheme.colors.textPrimary.copy(alpha = 0.7f),
            style = AppTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
    }

}
