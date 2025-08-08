package com.dezdeqness.feature.achievements.presentation.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dezdeqness.core.ui.views.image.AppImage
import com.dezdeqness.feature.achievements.R
import com.dezdeqness.feature.achievements.presentation.models.AchievementsUiModel

@Composable
fun AchievementCell(
    modifier: Modifier = Modifier,
    item: AchievementsUiModel
) {
    val isRuLocale = LocalConfiguration.current.locales.get(0).language == "ru"

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp)
            .clip(RoundedCornerShape(12.dp))
    ) {
        AppImage(
            data = item.imageUrl,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(12.dp),
            contentScale = ContentScale.Crop,
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.8f),
                        ),
                    )
                )
        )

        Column(
            modifier = Modifier
                .padding(4.dp)
                .align(Alignment.BottomStart),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {


            Text(
                if (isRuLocale) item.titleRu else item.titleEn,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            LinearProgressIndicator(
                progress = { item.progress },
                modifier = Modifier.fillMaxWidth(),
                drawStopIndicator = {}
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = stringResource(R.string.level_subtitle, item.level.toString()),
                    color = Color.White,
                    fontSize = 12.sp,
                )

                Text(
                    text = stringResource(R.string.level_progress, item.progressValue.toString()),
                    color = Color.White,
                    fontSize = 12.sp,
                )
            }
        }
    }
}
