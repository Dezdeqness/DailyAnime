package com.dezdeqness.presentation.routing

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut

private const val DURATION_SHORT = 300

fun AnimatedContentTransitionScope<*>.slideInFromStart(): EnterTransition =
    fadeIn(animationSpec = tween(DURATION_SHORT, easing = LinearEasing)) +
            slideIntoContainer(
                animationSpec = tween(DURATION_SHORT, easing = EaseIn),
                towards = AnimatedContentTransitionScope.SlideDirection.Start
            )

fun AnimatedContentTransitionScope<*>.slideOutToStart(): ExitTransition =
    fadeOut(animationSpec = tween(DURATION_SHORT, easing = LinearEasing)) +
            slideOutOfContainer(
                animationSpec = tween(DURATION_SHORT, easing = EaseOut),
                towards = AnimatedContentTransitionScope.SlideDirection.End
            )

fun AnimatedContentTransitionScope<*>.slideInFromBottom(): EnterTransition =
    slideIntoContainer(
        animationSpec = tween(DURATION_SHORT, easing = EaseIn),
        towards = AnimatedContentTransitionScope.SlideDirection.Up
    )

fun AnimatedContentTransitionScope<*>.slideOutToBottom(): ExitTransition =
    slideOutOfContainer(
        animationSpec = tween(DURATION_SHORT, easing = EaseOut),
        towards = AnimatedContentTransitionScope.SlideDirection.Down
    )

fun AnimatedContentTransitionScope<*>.slideInFromTop(): EnterTransition =
    slideIntoContainer(
        animationSpec = tween(DURATION_SHORT, easing = EaseIn),
        towards = AnimatedContentTransitionScope.SlideDirection.Down
    )

fun AnimatedContentTransitionScope<*>.slideOutToTop(): ExitTransition =
    slideOutOfContainer(
        animationSpec = tween(DURATION_SHORT, easing = EaseOut),
        towards = AnimatedContentTransitionScope.SlideDirection.Up
    )
