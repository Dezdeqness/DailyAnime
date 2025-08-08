package com.dezdeqness.feature.achievements.presentation.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dezdeqness.core.ui.views.shimmer

@Composable
fun ShimmerAchievementsLoading(
    modifier: Modifier = Modifier,
    times: Int = 3,
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState(), enabled = false)
    ) {
        repeat(times) { _ ->
            ShimmerAchievementsHeader()
            repeat(times) {
                ShimmerAchievementsItem()
            }
        }
    }
}

@Composable
private fun ShimmerAchievementsHeader(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .padding(vertical = 16.dp)
            .width(100.dp)
            .height(20.dp)
            .shimmer()
    )
}

@Composable
private fun ShimmerAchievementsItem(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .padding(bottom = 8.dp)
            .fillMaxWidth()
            .height(150.dp)
            .shimmer()
    )
}
