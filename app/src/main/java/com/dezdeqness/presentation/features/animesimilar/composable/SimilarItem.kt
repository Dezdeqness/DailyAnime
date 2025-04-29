package com.dezdeqness.presentation.features.animesimilar.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dezdeqness.R
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.presentation.action.Action
import com.dezdeqness.presentation.models.SimilarUiModel

@Composable
fun SimilarItem(
    modifier: Modifier = Modifier,
    item: SimilarUiModel,
    onClick: (Action) -> Unit,
) {
    val context = LocalContext.current

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .clickable(
                onClick = {
                    onClick(Action.AnimeClick(item.id, item.name))
                },
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = AppTheme.colors.ripple),
            )
            .fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.height(IntrinsicSize.Max)
        ) {

            Box {
                val model = remember(item.logoUrl) {
                    ImageRequest.Builder(context)
                        .data(item.logoUrl)
                        .crossfade(true)
                        .build()
                }

                AsyncImage(
                    model = model,
                    contentScale = ContentScale.FillHeight,
                    contentDescription = null,
                    placeholder = painterResource(id = R.drawable.ic_placeholder),
                    error = painterResource(id = R.drawable.ic_placeholder),
                    modifier = Modifier
                        .width(96.dp)
                        .height(120.dp)
                        .aspectRatio(2f / 3)
                )
            }

            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = item.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        maxLines = 2,
                        color = AppTheme.colors.textPrimary,
                        fontWeight = FontWeight.Bold,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = item.briefInfo,
                        textAlign = TextAlign.Start,
                        fontSize = 14.sp,
                        color = AppTheme.colors.textPrimary.copy(alpha = 0.8f),
                        modifier = Modifier.padding(start = 8.dp),
                    )
                }
            }

        }
    }
}
