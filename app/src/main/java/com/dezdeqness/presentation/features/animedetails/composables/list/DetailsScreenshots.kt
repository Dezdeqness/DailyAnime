package com.dezdeqness.presentation.features.animedetails.composables.list

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
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dezdeqness.R
import com.dezdeqness.core.ui.theme.AppTheme

@Composable
fun DetailsScreenshots(
    modifier: Modifier = Modifier,
    screenshots: List<String>,
    onScreenshotClick: (String) -> Unit
) {
    val context = LocalContext.current

    Column(modifier = modifier) {
        Text(
            stringResource(R.string.anime_details_screenshots_title),
            style = AppTheme.typography.headlineSmall,
            color = AppTheme.colors.textPrimary,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(screenshots.size) { index ->
                val url = screenshots[index]
                val model = remember {
                    ImageRequest.Builder(context)
                        .data(url)
                        .crossfade(true)
                        .build()
                }

                AsyncImage(
                    model = model,
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                    placeholder = painterResource(id = R.drawable.ic_placeholder),
                    error = painterResource(id = R.drawable.ic_placeholder),
                    modifier = Modifier
                        .width(120.dp)
                        .height(80.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable(
                            onClick = { onScreenshotClick(url) },
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(color = AppTheme.colors.ripple),
                        ),
                )
            }
        }
    }
}
