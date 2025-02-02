package com.dezdeqness.presentation.features.profile.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.dezdeqness.core.ui.shimmer
import com.dezdeqness.core.ui.theme.AppTheme

@Composable
fun ProfileSkeleton(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(AppTheme.colors.onPrimary)
            .verticalScroll(scrollState, enabled = false)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10))
                .width(80.dp)
                .height(80.dp)
                .shimmer()
        )
        Box(
            modifier = Modifier
                .padding(vertical = 16.dp)
                .height(28.dp)
                .width(100.dp)
                .shimmer()
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .height(20.dp)
                .width(140.dp)
                .shimmer()
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .height(20.dp)
                .width(140.dp)
                .shimmer()
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .height(20.dp)
                .width(140.dp)
                .shimmer()
        )

    }

}
