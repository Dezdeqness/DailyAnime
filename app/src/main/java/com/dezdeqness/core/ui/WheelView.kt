package com.dezdeqness.core.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.core.view.HapticFeedbackConstantsCompat
import androidx.core.view.ViewCompat
import kotlinx.coroutines.launch

@Composable
fun WheelView(
    modifier: Modifier = Modifier,
    itemSize: DpSize = DpSize(256.dp, 256.dp),
    selectedValue: Int = 0,
    itemCount: Int,
    rowOffset: Int = 3,
    onItemSelected: (Int) -> Unit,
    userScrollEnabled: Boolean = true,
    lazyWheelState: LazyListState? = null,
    content: @Composable LazyItemScope.(index: Int) -> Unit,
) {
    val view = LocalView.current

    val coroutineScope = rememberCoroutineScope()

    val count = itemCount + 2 * rowOffset
    val rowOffsetCount = maxOf(1, minOf(rowOffset, 4))
    val rowCount = ((rowOffsetCount * 2) + 1)
    val startIndex = selectedValue

    val state = lazyWheelState ?: rememberLazyListState(startIndex)

    val size = DpSize(itemSize.width, itemSize.height * rowCount)

    val isScrollInProgress = state.isScrollInProgress
    val focusedIndex = remember {
        derivedStateOf { state.firstVisibleItemIndex + rowOffsetCount }
    }

    LaunchedEffect(itemCount) {
        coroutineScope.launch {
            state.scrollToItem(startIndex)
        }
    }

    LaunchedEffect(isScrollInProgress) {
        if (!isScrollInProgress) {
            calculateIndexToFocus(state, size.height).let {
                val indexToFocus = ((it + rowOffsetCount) % count) - rowOffset

                onItemSelected(indexToFocus)
                if (state.firstVisibleItemScrollOffset != 0) {
                    coroutineScope.launch {
                        state.animateScrollToItem(it, 0)
                    }
                }
            }
        }
    }

    LaunchedEffect(state) {
        snapshotFlow { state.firstVisibleItemIndex }.collect {
            ViewCompat.performHapticFeedback(
                view,
                HapticFeedbackConstantsCompat.CLOCK_TICK
            )
        }
    }

    Box(modifier = modifier
        .height(size.height)
        .fillMaxWidth()
    ) {
        LazyColumn(
            modifier = Modifier
                .height(size.height)
                .fillMaxWidth(),
            state = state,
            userScrollEnabled = userScrollEnabled,
        ) {
            items(count) {
                val rotateDegree = calculateIndexRotation(focusedIndex.value, it, rowOffset)

                Box(
                    modifier = Modifier
                        .height(size.height / rowCount)
                        .fillMaxWidth()
                        .graphicsLayer {
                            this.rotationX = rotateDegree
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    if (it >= rowOffsetCount && it < itemCount + rowOffsetCount) {
                        content((it - rowOffsetCount) % itemCount)
                    }
                }

            }
        }

    }
}

private fun calculateIndexToFocus(listState: LazyListState, height: Dp): Int {
    val currentItem = listState.layoutInfo.visibleItemsInfo.firstOrNull()
    var index = currentItem?.index ?: 0

    if (currentItem?.offset != 0) {
        if (currentItem != null && currentItem.offset <= -height.value * 3 / 10) {
            index++
        }
    }
    return index
}

@Composable
private fun calculateIndexRotation(focusedIndex: Int, index: Int, offset: Int): Float {
    return (6 * offset + 1).toFloat() * (focusedIndex - index)
}

data class WheelData(val value: Int) {
    val displayName: String = if (value == -1) "" else value.toString().padStart(2, '0')
}
