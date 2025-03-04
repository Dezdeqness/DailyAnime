package com.dezdeqness.presentation.features.history.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dezdeqness.R
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.presentation.features.history.models.HistoryModel

@Composable
fun HistoryItem(modifier: Modifier = Modifier, item: HistoryModel.HistoryUiModel) {
    val context = LocalContext.current

    Card(modifier = modifier) {
        Box(contentAlignment = Alignment.Center) {
            val model = remember {
                ImageRequest.Builder(context)
                    .data(item.imageUrl)
                    .crossfade(true)
                    .build()
            }

            AsyncImage(
                model = model,
                contentScale = ContentScale.Crop,
                contentDescription = null,
                placeholder = painterResource(id = R.drawable.ic_placeholder),
                error = painterResource(id = R.drawable.ic_placeholder),
                colorFilter = ColorFilter.tint(
                    AppTheme.colors.onPrimary.copy(alpha = 0.7f),
                    blendMode = BlendMode.SrcOver
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth().padding(16.dp),
            ) {
                Text(
                    item.name,
                    color = AppTheme.colors.textPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                )

                Text(
                    item.action,
                    color = AppTheme.colors.textPrimary.copy(0.8f),
                    fontSize = 14.sp,
                )
            }
        }

    }
}
