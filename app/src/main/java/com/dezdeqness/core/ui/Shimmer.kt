package com.dezdeqness.core.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Composable
fun Modifier.shimmer(
    shape: Shape = RoundedCornerShape(6.dp),
    widthOfShadowBrush: Int = 700,
    angleOfAxisY: Float = 270f,
    durationMillis: Int = 1500,
    color: Color = Color.Unspecified
): Modifier = composed {
    val shimmerBaseColor = if (color == Color.Unspecified) {
        Color.LightGray
    } else {
        color
    }
    val shimmerColors = listOf(
        shimmerBaseColor.copy(alpha = 0.3f),
        shimmerBaseColor.copy(alpha = 0.5f),
        shimmerBaseColor.copy(alpha = 1.0f),
        shimmerBaseColor.copy(alpha = 0.5f),
        shimmerBaseColor.copy(alpha = 0.3f),
    )

    val transition = rememberInfiniteTransition(label = "")

    val translateAnimation = transition.animateFloat(
        initialValue = 0f,
        targetValue = (durationMillis + widthOfShadowBrush).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMillis,
                easing = LinearEasing,
            ),
            repeatMode = RepeatMode.Restart,
        ),
        label = "Shimmer loading animation",
    )

    return@composed this.then(
        this.background(
            brush = Brush.linearGradient(
                colors = shimmerColors,
                start = Offset(x = translateAnimation.value - widthOfShadowBrush, y = 0.0f),
                end = Offset(x = translateAnimation.value, y = angleOfAxisY),
            ),
            shape = shape
        )
    )
}
