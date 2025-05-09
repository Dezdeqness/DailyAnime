package com.dezdeqness.presentation.features.animedetails.composables.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.core.ui.BlurTransformation
import com.dezdeqness.presentation.models.HeaderItemUiModel

@Composable
fun DetailsHeader(
    modifier: Modifier = Modifier,
    detailsHeader: HeaderItemUiModel,
) {
    val context = LocalContext.current

    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
    ) {
        AsyncImage(
            model = remember(detailsHeader.imageUrl) {
                ImageRequest.Builder(context)
                    .data(detailsHeader.imageUrl)
                    .transformations(BlurTransformation(context))
                    .build()
            },
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    brush = Brush.verticalGradient(
                        listOf(Color.Transparent, AppTheme.colors.onPrimary),
                    )
                )
        )


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 56.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .size(width = 200.dp, height = 264.dp)
                    .clip(RoundedCornerShape(15.dp)),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = remember(detailsHeader.imageUrl) {
                        ImageRequest.Builder(context)
                            .data(detailsHeader.imageUrl)
                            .crossfade(true)
                            .build()
                    },
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .size(width = 200.dp, height = 264.dp)
                        .clip(RoundedCornerShape(15.dp)),
                    contentDescription = null,
                )

                Text(
                    detailsHeader.ratingScore.toString(),
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .background(Color.Black.copy(alpha = 0.8f), RoundedCornerShape(8.dp))
                        .padding(4.dp),
                    color = Color.White,
                    style = AppTheme.typography.headlineSmall,
                )
            }
        }
    }
}
