package com.dezdeqness.feature.forum.presentation.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dezdeqness.foundation.ui.theme.AppTheme
import com.dezdeqness.foundation.ui.views.shimmer

@Composable
fun ForumShimmerLoading(
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
        repeat(8) {
            ForumShimmerItem()
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun ForumShimmerItem() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .shimmer(shape = RoundedCornerShape(12.dp)),
        )
        Spacer(modifier = Modifier.width(14.dp))
        Box(
            modifier = Modifier
                .width(160.dp)
                .height(16.dp)
                .shimmer(shape = AppTheme.shapes.small),
        )
    }
}
