package com.dezdeqness.core.ui

import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.view.HapticFeedbackConstantsCompat
import androidx.core.view.ViewCompat
import com.dezdeqness.core.ui.theme.AppTheme
import kotlin.math.abs
import kotlin.math.exp
import kotlin.math.pow

@Composable
fun WheelView(
    modifier: Modifier = Modifier,
    items: List<String> = emptyList(),
    selectedItemIndex: Int = 0,
    onItemSelected: (index: Int) -> Unit,
) {
    val view = LocalView.current
    val itemHeight = 50.dp

    val visibleItems = 5

    val listState = rememberLazyListState(initialFirstVisibleItemIndex = selectedItemIndex)
    val firstVisibleItemIndexState = remember { derivedStateOf { listState.firstVisibleItemIndex } }

    val flingBehavior = rememberSnapFlingBehavior(listState)

    val offset by remember {
        derivedStateOf { listState.firstVisibleItemScrollOffset.toFloat() }
    }

    val centerIndex = firstVisibleItemIndexState.value + visibleItems / 2

    var lastCenterIndex by remember { mutableIntStateOf(centerIndex) }

    LaunchedEffect(centerIndex) {
        if (centerIndex != lastCenterIndex) {
            ViewCompat.performHapticFeedback(
                view,
                HapticFeedbackConstantsCompat.SEGMENT_FREQUENT_TICK
            )
            lastCenterIndex = centerIndex
            onItemSelected(centerIndex)
        }
    }

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState,
            flingBehavior = flingBehavior,
        ) {
            itemsIndexed(items) { index, item ->
                val distanceFromCenter = (index - centerIndex) - (offset / with(LocalDensity.current) { itemHeight.toPx() })
                val alphaVal = exp(-distanceFromCenter.pow(2) / 2)
                val scale = 1f - abs(distanceFromCenter) * 0.07f
                val angle = distanceFromCenter * 15f
                val translationY1 = distanceFromCenter *  with(LocalDensity.current) { itemHeight.toPx() } * 0.15f

                Row(
                    modifier = Modifier
                        .height(itemHeight)
                        .fillMaxWidth()
                        .graphicsLayer {
                            scaleY = scale
                            translationY = translationY1
                            rotationX = angle
                            alpha = alphaVal
                            transformOrigin = TransformOrigin.Center
                            cameraDistance = itemHeight.toPx() * density * 800
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = item,
                        textAlign = TextAlign.Center,
                        color = AppTheme.colors.textPrimary.copy(alpha = alphaVal)
                    )
                }
            }
        }
    }
}
