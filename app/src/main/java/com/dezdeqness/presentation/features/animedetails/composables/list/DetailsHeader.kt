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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.core.ui.views.image.AppImage
import com.dezdeqness.presentation.models.HeaderItemUiModel

@Composable
fun DetailsHeader(
    modifier: Modifier = Modifier,
    detailsHeader: HeaderItemUiModel,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
    ) {
        AppImage(
            data = detailsHeader.imageUrl,
            shape = RoundedCornerShape(0.dp),
            modifier = Modifier.matchParentSize(),
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
                AppImage(
                    data = detailsHeader.imageUrl,
                    contentScale = ContentScale.FillBounds,
                    shape = RoundedCornerShape(15.dp),
                    modifier = Modifier.size(width = 200.dp, height = 264.dp),
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
