package com.dezdeqness.presentation.features.animelist.composable

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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dezdeqness.R
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.presentation.features.animelist.AnimeUiModel

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
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(item.logoUrl)
                    .crossfade(true)
                    .build(),
                contentScale = ContentScale.Crop,
                contentDescription = null,
                placeholder = painterResource(id = R.drawable.ic_placeholder),
                error = painterResource(id = R.drawable.ic_placeholder),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .align(alignment = Alignment.Center)
            )

            Text(
                text = item.kind,
                color = colorResource(id = R.color.search_label),
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(colorResource(id = R.color.background_shadow))
                    .padding(4.dp)
                    .align(alignment = Alignment.TopStart)
            )
        }

        Box(contentAlignment = Alignment.Center) {
            Text(
                text = item.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                textAlign = TextAlign.Center,
                maxLines = 2,
                color = AppTheme.colors.textPrimary,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
            )

            Text(
                text = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                minLines = 2,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AnimeItemPreview() {
    AppTheme {
        Box(
            modifier = Modifier.width(200.dp)
        ) {
            AnimeItem(
                item = AnimeUiModel(
                    id = 321,
                    logoUrl = "url",
                    title = "Code Geass",
                    kind = "TV",
                ),
                onClick = {},
            )
        }
    }
}
