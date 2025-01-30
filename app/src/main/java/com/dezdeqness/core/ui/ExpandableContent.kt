package com.dezdeqness.core.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment

private const val TRANSITION_DURATION = 300

@Composable
fun ExpandableContent(
    isVisible: Boolean,
    content: @Composable () -> Unit,
) {

    val enterTransition = remember {
        expandVertically(
            expandFrom = Alignment.Top,
            animationSpec = tween(
                durationMillis = TRANSITION_DURATION
            )
        ) + fadeIn(
            initialAlpha = 0.3f,
            animationSpec = tween(
                durationMillis = TRANSITION_DURATION
            )
        )
    }
    val exitTransition = remember {
        shrinkVertically(
            shrinkTowards = Alignment.Top,
            animationSpec = tween(
                durationMillis = TRANSITION_DURATION
            )
        ) + fadeOut(
            animationSpec = tween(
                durationMillis = TRANSITION_DURATION
            )
        )
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = enterTransition,
        exit = exitTransition,
    ) {
        content()
    }
}
