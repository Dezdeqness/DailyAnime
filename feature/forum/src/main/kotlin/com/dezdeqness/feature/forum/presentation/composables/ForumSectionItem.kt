package com.dezdeqness.feature.forum.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dezdeqness.feature.forum.presentation.models.ForumUiModel
import com.dezdeqness.foundation.ui.theme.AppTheme

@Composable
fun ForumSectionItem(
    item: ForumUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val visuals = forumIconVisualsFor(item.permalink)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colors.onPrimary,
        ),
        shape = AppTheme.shapes.large,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        color = visuals.tint.copy(alpha = 0.16f),
                        shape = RoundedCornerShape(12.dp),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = visuals.icon,
                    contentDescription = null,
                    tint = visuals.tint,
                    modifier = Modifier.size(22.dp),
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Text(
                text = item.name,
                color = AppTheme.colors.textPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
            )

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = AppTheme.colors.textSecondary,
                modifier = Modifier.size(20.dp),
            )
        }
    }
}

private data class ForumIconVisuals(
    val icon: ImageVector,
    val tint: Color,
)

private val ForumFallbackVisuals = ForumIconVisuals(
    icon = Icons.AutoMirrored.Filled.Chat,
    tint = Color(0xFFA0A0FF),
)

private val ForumIconByPermalink: Map<String, ForumIconVisuals> = mapOf(
    "animanga" to ForumIconVisuals(
        icon = Icons.Filled.Tv,
        tint = Color(0xFF7EE6C3),
    ),
    "news" to ForumIconVisuals(
        icon = Icons.Filled.Newspaper,
        tint = Color(0xFFFF8F8F),
    ),
    "site" to ForumIconVisuals(
        icon = Icons.Filled.Public,
        tint = Color(0xFF64B5F6),
    ),
    "offtopic" to ForumIconVisuals(
        icon = Icons.AutoMirrored.Filled.Chat,
        tint = Color(0xFF64C7F6),
    ),
    "vn" to ForumIconVisuals(
        icon = Icons.AutoMirrored.Filled.MenuBook,
        tint = Color(0xFFE389E5),
    ),
    "games" to ForumIconVisuals(
        icon = Icons.Filled.SportsEsports,
        tint = Color(0xFFA0A0FF),
    ),
    "clubs" to ForumIconVisuals(
        icon = Icons.Filled.Groups,
        tint = Color(0xFF82D0F2),
    ),
    "critiques" to ForumIconVisuals(
        icon = Icons.Filled.Star,
        tint = Color(0xFFFFD188),
    ),
    "reviews" to ForumIconVisuals(
        icon = Icons.Filled.RateReview,
        tint = Color(0xFFFFC36E),
    ),
    "contests" to ForumIconVisuals(
        icon = Icons.Filled.EmojiEvents,
        tint = Color(0xFFFFCD66),
    ),
    "collections" to ForumIconVisuals(
        icon = Icons.Filled.Collections,
        tint = Color(0xFFB4C4D6),
    ),
    "articles" to ForumIconVisuals(
        icon = Icons.AutoMirrored.Filled.Article,
        tint = Color(0xFFB4D6CE),
    ),
    "cosplay" to ForumIconVisuals(
        icon = Icons.Filled.Brush,
        tint = Color(0xFFFF9FCB),
    ),
)

private fun forumIconVisualsFor(permalink: String): ForumIconVisuals =
    ForumIconByPermalink[permalink] ?: ForumFallbackVisuals
